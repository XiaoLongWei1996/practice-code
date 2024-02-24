package com.xlw.test.uploader_demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xlw.test.uploader_demo.entity.FileStore;

/**
 * 文件储存表(FileStore)表服务接口
 *
 * @author xlw
 * @since 2024-02-24 14:47:05
 */
public interface FileStoreService extends IService<FileStore> {
    
     /**
     * 分页查询
     *
     * @param page 页面
     * @return {@link IPage}<{@link FileStore}>
     */
    IPage<FileStore> listByPage(IPage<?> page);

    FileStore createUploader(FileStore fileStore);
}

