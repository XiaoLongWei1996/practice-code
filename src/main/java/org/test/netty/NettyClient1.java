package org.test.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @Title: NettyClient1
 * @Author xlw
 * @Package org.test.netty
 * @Date 2024/4/24 11:20
 */
public class NettyClient1 {

    public static void createClient2() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture cf = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //字符串编码,发送数据  String => ByteBuf 编码发出
                        //ch.pipeline().addLast(new StringEncoder()).addLast(new StringDecoder());
                        //字符串解码,接受数据  ByteBuf => String 解码接收
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                System.out.println("客户端收到消息" + Thread.currentThread().getName() + ":" + msg);
                                //关闭通道,异步操作
                                ctx.channel().close();
                            }
                        });
                    }
                })
                .connect(new InetSocketAddress("localhost", 8888));
        cf.addListeners(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    System.out.println("连接失败");
                    return;
                }
                future.channel().writeAndFlush(Unpooled.copiedBuffer("服务器你好".getBytes(StandardCharsets.UTF_8)));
            }
        });

        //监控关闭操作是否成功
        cf.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    //关闭EventLoopGroup
                    group.shutdownGracefully();
                    System.out.println("客户端关闭");
                }
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        createClient2();
    }
}
