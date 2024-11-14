package org.test.util;


import io.netty.buffer.ByteBuf;

/**
 * @description: ByteBuf工具类
 * @Title: ByteBufUtil
 * @Author xlw
 * @Package org.test.util
 * @Date 2024/11/14 14:43
 */
public class ByteBufUtil {

    private static String NEWLINE = "\r\n";

    public static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        io.netty.buffer.ByteBufUtil.appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}
