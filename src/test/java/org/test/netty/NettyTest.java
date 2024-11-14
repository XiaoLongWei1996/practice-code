package org.test.netty;


import cn.hutool.core.util.NumberUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.CompositeByteBuf;
import lombok.Data;
import org.junit.Test;
import org.test.util.ByteBufUtil;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @Title: NettyTest
 * @Author xlw
 * @Package org.test.netty
 * @Date 2024/11/14 14:23
 */
public class NettyTest {

    @Test
    public void test(){
        //ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(1024);
        //buffer.writeBytes("hello world".getBytes(StandardCharsets.UTF_8));
        //buffer.release();
        //ByteBufUtil.log(buffer);
        Student student = new Student();
        student.setMoney(NumberUtil.toBigDecimal("0.00"));
        System.out.println(NumberUtil.toBigDecimal("0.00").compareTo(NumberUtil.toBigDecimal(0)));
    }

    @Data
    private static class Student {
        private BigDecimal money;
    }
}
