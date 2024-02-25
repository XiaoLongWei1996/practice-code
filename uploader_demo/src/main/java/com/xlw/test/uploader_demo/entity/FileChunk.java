package com.xlw.test.uploader_demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 文件分片表(FileChunk)表实体类
 *
 * @author xlw
 * @since 2024-02-24 14:47:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("file_chunk")
public class FileChunk {

    //id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //文件id
    @TableField(value = "file_id")
    private Integer fileId;

    //分片名称
    @TableField(value = "chunk_name")
    private String chunkName;

    //分片大小
    @TableField(value = "chunk_size")
    private Long chunkSize;

    //分片次序
    @TableField(value = "chunk_number")
    private Integer chunkNumber;

    //MD5
    @TableField(value = "md5")
    private String md5;

    //创建时间
    @TableField(value = "create_dt", fill = FieldFill.INSERT)
    private Date createDt;

    //修改时间
    @TableField(value = "update_dt", fill = FieldFill.INSERT_UPDATE)
    private Date updateDt;

}
