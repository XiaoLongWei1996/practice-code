package org.test.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @description: TCP/IP 中消息传输基于流的方式，没有边界;协议的目的就是划定消息的边界，制定通信双方要共同遵守的通信规则,常见的协议：http、ssh等
 * @Title: HttpNettyServer
 * @Author xlw
 * @Package org.test.netty
 * @Date 2023/9/28 18:00
 */
public class Agreement {

    /**
     * 请求redis
     * 如果我们要向Redis服务器发送一条set name Nyima的指令，需要遵守如下协议
     * *3\r\n$3\r\nset\r\n$4\r\nname\r\n$5\r\nNyima\r\n
     */
    public static void requestRedis() {
        EventLoopGroup work = new NioEventLoopGroup(1);
        try {
            Bootstrap client = new Bootstrap();
            ChannelFuture cf = client
                    .group(work)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            // 打印日志
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    // 回车与换行符
                                    final byte[] LINE = {'\r', '\n'};
                                    // 获得ByteBuf
                                    ByteBuf buffer = ctx.alloc().buffer();
                                    // 连接建立后，向Redis中发送一条指令，注意添加回车与换行
                                    // set name Nyima
                                    // 该指令一共有3部分，每条指令之后都要添加回车与换行符
                                    //buffer.writeBytes("*3".getBytes());
                                    //buffer.writeBytes(LINE);
                                    //// 第一个指令的长度是3
                                    //buffer.writeBytes("$3".getBytes());
                                    //buffer.writeBytes(LINE);
                                    //// 第一个指令是set指令
                                    //buffer.writeBytes("set".getBytes());
                                    //buffer.writeBytes(LINE);
                                    //// 第二个指令的长度是4
                                    //buffer.writeBytes("$4".getBytes());
                                    //buffer.writeBytes(LINE);
                                    //// 第二个指令是name
                                    //buffer.writeBytes("name".getBytes());
                                    //buffer.writeBytes(LINE);
                                    //// 第三个指令的长度是5
                                    //buffer.writeBytes("$5".getBytes());
                                    //buffer.writeBytes(LINE);
                                    //// 第三个指令是Nyima
                                    //buffer.writeBytes("Nyima".getBytes());
                                    //buffer.writeBytes(LINE);
                                    //发送auth smark@park 命令
                                    buffer.writeBytes("*2".getBytes());
                                    buffer.writeBytes(LINE);
                                    buffer.writeBytes("$4".getBytes());
                                    buffer.writeBytes(LINE);
                                    buffer.writeBytes("auth".getBytes());
                                    buffer.writeBytes(LINE);
                                    buffer.writeBytes("$10".getBytes());
                                    buffer.writeBytes(LINE);
                                    buffer.writeBytes("smark@park".getBytes());
                                    buffer.writeBytes(LINE);
                                    //发送数据
                                    ctx.writeAndFlush(buffer);
                                    System.out.println("发送数据");
                                    super.channelActive(ctx);
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    ByteBuf byteBuf = (ByteBuf) msg;
                                    System.out.println(byteBuf.toString());
                                    super.channelRead(ctx, msg);
                                }
                            });
                        }
                    })
                    .connect("192.168.1.154", 6379);
            cf.sync();
            cf.channel().close().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            work.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        requestRedis();
    }

}
