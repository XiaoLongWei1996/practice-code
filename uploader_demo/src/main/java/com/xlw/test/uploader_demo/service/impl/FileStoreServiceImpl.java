package com.xlw.test.uploader_demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlw.test.uploader_demo.config.enums.FileType;
import com.xlw.test.uploader_demo.dao.FileStoreMapper;
import com.xlw.test.uploader_demo.entity.DownloadTask;
import com.xlw.test.uploader_demo.entity.FileChunk;
import com.xlw.test.uploader_demo.entity.FileStore;
import com.xlw.test.uploader_demo.service.FileChunkService;
import com.xlw.test.uploader_demo.service.FileStoreService;
import com.xlw.test.uploader_demo.util.FileUtil;
import com.xlw.test.uploader_demo.util.MinioUtil;
import io.minio.messages.Part;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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

    @Resource
    private FileChunkService fileChunkService;

    @Value("${minio.bucket}")
    private String bucketName;

    private ThreadPoolExecutor executor;

    private LinkedBlockingQueue<DownloadTask> blockingQueue = new LinkedBlockingQueue(5);

    @PostConstruct
    public void init() {
        executor = new ThreadPoolExecutor(2,
                2,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(5),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }

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
                Long l = fileStore.getSize() / FileUtil.FILE_SLICE_SIZE;
                if (fileStore.getSize() % FileUtil.FILE_SLICE_SIZE != 0) {
                    l += 1;
                }
                fileStore.setTotalChunk(l.intValue());
            } else {
                fileStore.setTotalChunk(0);
            }
            if (save(fileStore)) {
                createChunk(fileStore);
            }
            fs = this.getOne(wrapper);
        }
        QueryWrapper<FileChunk> chunkWrapper = new QueryWrapper<>();
        chunkWrapper.eq("file_id", fs.getId());
        List<FileChunk> list = fileChunkService.list(chunkWrapper);
        fs.setFileChunks(list);
        return fs;
    }

    private boolean createChunk(FileStore fileStore) {
        if (fileStore.getTotalChunk() > 0) {
            List<FileChunk> fileChunks = new ArrayList<>(fileStore.getTotalChunk());
            long size = fileStore.getSize();
            for (int i = 1; i <= fileStore.getTotalChunk(); i++) {
                FileChunk fileChunk = new FileChunk();
                fileChunk.setChunkNumber(i);
                fileChunk.setChunkName(fileStore.getFileName() + "_" + i);
                fileChunk.setChunkSize(Math.min(FileUtil.FILE_SLICE_SIZE, size - (i - 1) * FileUtil.FILE_SLICE_SIZE));
                fileChunk.setFileId(fileStore.getId());
                fileChunks.add(fileChunk);
            }
            return fileChunkService.saveBatch(fileChunks);
        }
        return false;
    }

    @Override
    public String getUploadUrl(FileChunk fileChunk) {
        FileStore fs = getById(fileChunk.getFileId());
        Map<String, String> m = new HashMap<>();
        m.put("partNumber", String.valueOf(fileChunk.getChunkNumber()));
        m.put("uploadId", fs.getUploadId());
        if (fileChunkService.updateById(fileChunk)) {
            return minioUtil.getMultipartUploadUrl(bucketName, fs.getFileName(), m);
        }
        return null;
    }

    @Override
    public List<Part> partList(Integer id) {
        FileStore fs = getById(id);
        List<Part> parts = minioUtil.listPartsAsync(bucketName, fs.getFileName(), fs.getTotalChunk(), fs.getUploadId());
        for (Part part : parts) {
            System.out.println(part.partNumber() + " " + part.etag() + " " + part.partSize() + " " +part.lastModified());
        }
        return parts;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FileStore mergeFile(Integer id) {
        FileStore fs = getById(id);
        List<Part> parts = minioUtil.listPartsAsync(bucketName, fs.getFileName(), fs.getTotalChunk(), fs.getUploadId());
        if (fs.getTotalChunk() != parts.size()) {
            throw new RuntimeException("分片数量不一致");
        }
        Part[] arr = parts.stream().toArray(Part[]::new);
        minioUtil.completeMultipartUploadAsync(bucketName, fs.getFileName(), fs.getUploadId(), arr);
        fs.setComplete(1);
        updateById(fs);
        return fs;
    }

    @Override
    public void partUpload(MultipartFile file, FileChunk fileChunk) {
        FileStore fs = getById(fileChunk.getFileId());
        try(InputStream inputStream = file.getInputStream()) {
            minioUtil.uploadPartAsync(bucketName, fs.getFileName(), inputStream, file.getSize(), fs.getUploadId(), fileChunk.getChunkNumber());
            fileChunkService.updateById(fileChunk);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @SneakyThrows(Exception.class)
    @Override
    public void delayDownload(String fileName, HttpServletResponse response) {
        //线程池方式实现
        Future<Boolean> submit = executor.submit(() -> {
            minioUtil.download(bucketName, fileName, response, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            return true;
        });
        submit.get();

        //自定义队列方式实现
        //DownloadTask downloadTask = new DownloadTask(fileName, response);
        ////阻塞入队列，入队列成功的才能执行，否则等待
        //boolean b = blockingQueue.offer(downloadTask, 2, TimeUnit.SECONDS);
        //System.out.println(blockingQueue);
        //if (!b) {
        //    throw new RuntimeException("队列已满");
        //}
        //try {
        //    minioUtil.download(bucketName, fileName, response, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        //} catch (Exception e) {
        //    //异常删除数据
        //    blockingQueue.remove(downloadTask);
        //    throw new RuntimeException(e);
        //}
        ////处理完任务删除元素
        //blockingQueue.remove(downloadTask);
    }
}

