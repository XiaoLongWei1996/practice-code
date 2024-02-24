package com.xlw.test.uploader_demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xlw.test.uploader_demo.entity.FileStore;
import org.apache.ibatis.annotations.Select;

/**
 * 文件储存表(FileStore)表数据库访问层
 *
 * @author xlw
 * @since 2024-02-24 14:47:01
 */
public interface FileStoreMapper extends BaseMapper<FileStore> {
    
    @Select("select file_namereal_namesuffixmd5file_typecontent_typesizeimg_coverversiondeletetotal_chunkcreate_dtupdate_dt from file_store")
    IPage<FileStore> listByPage(IPage<?> page);
    
}

