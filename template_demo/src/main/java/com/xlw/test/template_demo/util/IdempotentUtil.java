package com.xlw.test.template_demo.util;


import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.cron.timingwheel.SystemTimer;
import cn.hutool.cron.timingwheel.TimerTask;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @description: 幂等性工具类
 * @Title: IdempotentUtil
 * @Author xlw
 * @Package com.invoice.tcc.util
 * @Date 2024/11/18 12:51
 */
@Slf4j
public class IdempotentUtil {


    /**
     * 时间轮
     */
    private static final SystemTimer SYSTEM_TIMER = new SystemTimer();

    /**
     * 幂等集
     */
    private static final ConcurrentHashSet<String> IDEMPOTENT_SET = new ConcurrentHashSet<>();

    /**
     * 幂等时间,1分钟
     */
    public static final long IDEMPOTENT_TIME = 60000;

    static {
        SYSTEM_TIMER.start();
    }

    /**
     * 幂等性验证
     *
     * @param qysh  企业税号
     * @param zrrmc 自然人名称
     * @param jshj  价税合计
     */
    public static void verify(String qysh, String zrrmc, BigDecimal jshj) {
        String key = qysh + zrrmc + jshj.multiply(NumberUtil.toBigDecimal(100)).intValue();
        verify(key, IDEMPOTENT_TIME);
    }

    /**
     * 幂等性验证
     *
     * @param key            key
     * @param idempotentTime 幂等时间,单位毫秒
     */
    public static void verify(String key, long idempotentTime) {
        if (IDEMPOTENT_SET.contains(key)) {
            throw new RuntimeException("请勿重复提交请求，一分钟后再试");
        }
        boolean b = IDEMPOTENT_SET.add(key);
        if (b) {
            log.info("添加幂等key: {}", key);
            SYSTEM_TIMER.addTask(new TimerTask(() -> IDEMPOTENT_SET.remove(key), idempotentTime));
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                IdempotentUtil.verify("a123", "张三", new BigDecimal(100.00));
            }).start();
        }
        Thread.sleep(1000);
        System.out.println(IDEMPOTENT_SET);
        Thread.sleep(2000);
        System.out.println(IDEMPOTENT_SET);
    }

}
