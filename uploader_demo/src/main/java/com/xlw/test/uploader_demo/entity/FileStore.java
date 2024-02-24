package com.xlw.test.uploader_demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 文件储存表(FileStore)表实体类
 *
 * @author xlw
 * @since 2024-02-24 14:47:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("file_store")
public class FileStore {

    //id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //文件名称
    @TableField(value = "file_name")
    private String fileName;

    //真实名称
    @TableField(value = "real_name")
    private String realName;

    //文件后缀
    @TableField(value = "suffix")
    private String suffix;

    //文件md5
    @TableField(value = "md5")
    private String md5;

    //文件类型（1：文档，2：图片，3：音频，4：视频，5：其他）
    @TableField(value = "file_type")
    private Integer fileType;

    //内容类型
    @TableField(value = "content_type")
    private String contentType;

    //文件大小
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "size")
    private Long size;

    //文件封面
    @TableField(value = "img_cover")
    private String imgCover;

    //版本号
    @TableField(value = "version")
    private String version;

    //是否删除（0：否，1：是）
    @TableLogic
    @TableField(value = "del")
    private Integer del;

    //分片数（0：则是不需要分片）
    @TableField(value = "total_chunk")
    private Integer totalChunk;

    //上传id
    @TableField(value = "upload_id")
    private String uploadId;

    //是否上传完成（0：否，1：是）
    @TableField(value = "complete")
    private Integer complete;

    //创建时间
    @TableField(value = "create_dt", fill = FieldFill.INSERT)
    private Date createDt;

    //创建时间
    @TableField(value = "update_dt", fill = FieldFill.INSERT_UPDATE)
    private Date updateDt;

}
