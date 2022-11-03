package com.springcloud.test.alibabaconsumer.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 肖龙威
 * @date 2022/11/03 16:19
 */
@Component
public class MyRequestOriginParser implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        // 1.获取请求头
        String origin = httpServletRequest.getHeader("origin");
        // 2.非空判断
        if (StrUtil.isBlank(origin)) {
            origin = "blank";
        }
        return origin;
    }
}
