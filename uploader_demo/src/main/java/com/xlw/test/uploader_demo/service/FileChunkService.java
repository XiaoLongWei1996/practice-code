package com.xlw.test.uploader_demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xlw.test.uploader_demo.entity.FileChunk;

/**
 * 文件分片表(FileChunk)表服务接口
 *
 * @author xlw
 * @since 2024-02-24 14:47:09
 */
public interface FileChunkService extends IService<FileChunk> {
    
     /**
     * 分页查询
     *
     * @param page 页面
     * @return {@link IPage}<{@link FileChunk}>
     */
    IPage<FileChunk> listByPage(IPage<?> page);
}

