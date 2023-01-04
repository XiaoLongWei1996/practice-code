package com.test.springboot.domain;

import lombok.Builder;
import lombok.Data;

import java.io.File;

/**
 * @author 肖龙威
 * @date 2022/12/06 13:53
 */
@Builder
@Data
public class FileDetail {

    private Integer id;

    private File file;

    private String effect;

    private Double time;

    private String format;

    /**----------------------音视频----------------------*/

    private Integer startTime;

    private Integer endTime;

    /** 是否静音 */
    private Integer mute;
}
