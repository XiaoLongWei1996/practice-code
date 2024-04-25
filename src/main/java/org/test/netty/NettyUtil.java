package org.test.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.internal.StringUtil;

/**
 * @description:
 * @Title: NettyUtil
 * @Author xlw
 * @Package org.test.netty
 * @Date 2024/4/25 10:13
 */
public class NettyUtil {

    public static void printByteBuf(ByteBuf byteBuf) {
        StringBuilder builder = new StringBuilder()
                .append(" read index:").append(byteBuf.readerIndex())//获取读索引
                .append(" write index:").append(byteBuf.writerIndex()) //获取写索引
                .append(" capacity:").append(byteBuf.capacity())//获取容量
                .append(StringUtil.NEWLINE);
        //把ByteBuf中的内容，dump到StringBuilder中
        ByteBufUtil.appendPrettyHexDump(builder, byteBuf);
        System.out.println(builder.toString());
    }
}
