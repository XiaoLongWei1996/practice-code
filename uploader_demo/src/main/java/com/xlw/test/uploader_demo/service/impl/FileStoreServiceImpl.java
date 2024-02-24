package com.xlw.test.uploader_demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlw.test.uploader_demo.config.enums.FileType;
import com.xlw.test.uploader_demo.dao.FileStoreMapper;
import com.xlw.test.uploader_demo.entity.FileStore;
import com.xlw.test.uploader_demo.service.FileStoreService;
import com.xlw.test.uploader_demo.util.FileUtil;
import com.xlw.test.uploader_demo.util.MinioUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 文件储存表(FileStore)表服务实现类
 *
 * @author xlw
 * @since 2024-02-24 14:47:06
 */
@Service("fileStoreService")
public class FileStoreServiceImpl extends ServiceImpl<FileStoreMapper, FileStore> implements FileStoreService {

    @Resource
    private MinioUtil minioUtil;

    @Value("${minio.bucket}")
    private String bucketName;

    private static final int FILE_SLICE_SIZE = 1024 * 1024 * 10;

    @Override
    public IPage<FileStore> listByPage(IPage<?> page) {
        return getBaseMapper().listByPage(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FileStore createUploader(FileStore fileStore) {
        QueryWrapper<FileStore> wrapper = new QueryWrapper<>();
        wrapper.eq("md5", fileStore.getMd5());
        FileStore fs = this.getOne(wrapper);
        if (fs == null) {
            String fileName = FileUtil.getFileName(fileStore.getRealName());
            fileStore.setFileName(fileName);
            //创建上传请求
            String uploadId = minioUtil.createMultipartUploadAsync(bucketName, fileName);
            fileStore.setUploadId(uploadId);
            fileStore.setSuffix(FileUtil.getFileSuffix(fileStore.getRealName()));
            fileStore.setFileType(FileType.getFileType(fileStore.getSuffix()));
            fileStore.setContentType(FileUtil.getMimeType(fileStore.getRealName()));
            //检查是否需要分片
            if (FileUtil.checkIsChunk(fileStore.getSize())) {
                Long l = fileStore.getSize() / FILE_SLICE_SIZE;
                if (fileStore.getSize() % FILE_SLICE_SIZE != 0) {
                    l += 1;
                }
                fileStore.setTotalChunk(l.intValue());
            }
            save(fileStore);
            fs = this.getOne(wrapper);
        }
        return fs;
    }
}

