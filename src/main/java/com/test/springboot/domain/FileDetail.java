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

    private Integer time;

    private String format;
}
