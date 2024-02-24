package com.xlw.test.uploader_demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xlw.test.uploader_demo.entity.FileChunk;
import org.apache.ibatis.annotations.Select;

/**
 * 文件分片表(FileChunk)表数据库访问层
 *
 * @author xlw
 * @since 2024-02-24 14:47:09
 */
public interface FileChunkMapper extends BaseMapper<FileChunk> {
    
    @Select("select file_idchunk_namechunk_sizechunk_numbermd5create_dtupdate_dt from file_chunk")
    IPage<FileChunk> listByPage(IPage<?> page);
    
}

