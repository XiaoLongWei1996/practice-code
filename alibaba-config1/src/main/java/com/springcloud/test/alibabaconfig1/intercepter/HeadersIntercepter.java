package com.springcloud.test.alibabaconfig1.intercepter;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;

/**
 * @author 肖龙威
 * @date 2022/11/04 16:44
 */
//@Component
public class HeadersIntercepter implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes!=null){
            Enumeration<String> headerNames = attributes.getRequest().getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                requestTemplate.header(name, attributes.getRequest().getHeader(name));
            }
        }

    }
}
