package com.xlw.test.template_demo.config;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xlw
 * @description:
 * @title: Param
 * @package com.xlw.test.template_demo.config
 * @date 2025/4/27 10:29
 */
@Data
public class Param {

    private String name;

    private MultipartFile file;
}
