package com.xlw.test.template_demo.config;


import cn.hutool.core.util.IdUtil;
import org.apache.logging.log4j.ThreadContext;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xlw
 * @description: 日志跟踪过滤器
 * @title: TraceIdFilter
 * @package com.xlw.test.template_demo.config
 * @date 2025/3/17 18:01
 */
@WebFilter(filterName = "TraceIdFilter", urlPatterns = "/*")
public class TraceIdFilter implements Filter {

    private static final String TRACE_ID = "traceId";

    /**
     * 初始化
     *
     * @param filterConfig 过滤器配置
     * @throws ServletException Servlet 异常
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        // 生成一个随机数给到前端
        String traceId = IdUtil.fastSimpleUUID();
        try {
            // 随机数放到此线程的上下文中，可以在每条日志前加入。具体看下面log4j2.xml
            ThreadContext.put(TRACE_ID, traceId);
            // 随机数放到Header中，在ResponseHeaders中可查看到此数据
            resp.addHeader(TRACE_ID, traceId);
            filterChain.doFilter(req, resp);
        } finally {
            ThreadContext.clearAll();
        }
    }

    /**
     * 摧毁
     */
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
