package com.xlw.test.template_demo.util;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * @author xlw
 * @description: servlet增强工具类
 * @title: ServletEnhanceUtil
 * @package com.xlw.test.template_demo.util
 * @date 2025/3/17 17:27
 */
public class ServletEnhanceUtil {

    /**
     * 获得请求
     *
     * @return {@link HttpServletRequest }
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取响应
     *
     * @return {@link HttpServletResponse }
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
    }

    /**
     * 获取参数
     *
     * @return {@link String }
     */
    public static String getParameter() {
        Map<String, String> paramMap = ServletUtil.getParamMap(getRequest());
        String params = "";
        if (CollUtil.isEmpty(paramMap)) {
            return params;
        }
        JSONConfig config = new JSONConfig();
        config.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        config.setIgnoreNullValue(false);
        params = JSONUtil.toJsonStr(paramMap, config);
        return params;
    }

    /**
     * 获取body
     *
     * @return {@link String }
     */
    public static String getBody() {
        return ServletUtil.getBody(getRequest());
    }

    /**
     * 获取客户端 IP
     *
     * @return {@link String }
     */
    public static String getClientIp() {
        return ServletUtil.getClientIP(getRequest());
    }

    /**
     * 获取 URI
     *
     * @return {@link String }
     */
    public static String getUri() {
        return getRequest().getRequestURI();
    }

    /**
     * 获取请求方法
     *
     * @return {@link String }
     */
    public static String getMethod() {
        return getRequest().getMethod();
    }

    /**
     * 获取url
     *
     * @return {@link String }
     */
    public static String getUrl() {
        return getRequest().getRequestURL().toString();
    }
}
