package com.xlw.test.template_demo.config.listener;

import cn.hutool.core.util.RandomUtil;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * @description: traceID监听器
 * @Title: TraceIdListener
 * @Author xlw
 * @Package com.sxkj.pay.config
 * @Date 2024/8/8 15:49
 */
@Component
public class TraceIdListener implements ServletRequestListener {

    private static final String TRACE_ID = "traceId";

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        ThreadContext.clearAll();
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        String traceId = RandomUtil.randomString(32);
        ThreadContext.put(TRACE_ID, traceId);
    }
}
