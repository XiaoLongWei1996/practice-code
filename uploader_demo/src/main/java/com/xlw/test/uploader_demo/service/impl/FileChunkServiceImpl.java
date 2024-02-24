package com.xlw.test.uploader_demo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlw.test.uploader_demo.dao.FileChunkMapper;
import com.xlw.test.uploader_demo.entity.FileChunk;
import com.xlw.test.uploader_demo.service.FileChunkService;
import org.springframework.stereotype.Service;

/**
 * 文件分片表(FileChunk)表服务实现类
 *
 * @author xlw
 * @since 2024-02-24 14:47:09
 */
@Service("fileChunkService")
public class FileChunkServiceImpl extends ServiceImpl<FileChunkMapper, FileChunk> implements FileChunkService {

    @Override
    public IPage<FileChunk> listByPage(IPage<?> page) {
        return getBaseMapper().listByPage(page);
    }
}

