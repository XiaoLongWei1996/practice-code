package org.test.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.UnsupportedEncodingException;
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

    public static void createClient3() throws InterruptedException, UnsupportedEncodingException {
        NioEventLoopGroup work = new NioEventLoopGroup(1);
        Bootstrap client = new Bootstrap();
        ChannelFuture cf = client
                .group(work)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //设置编码解码器
                        ch.pipeline().addLast(Agreement.MY_MESSAGE_CODEC);
                        //设置入站
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<Agreement.MessageProtocol>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, Agreement.MessageProtocol msg) throws Exception {
                                System.out.println("客户端收到消息:" + new String(msg.getContent(), "UTF-8"));
                            }
                        });
                    }
                })
                .connect(new InetSocketAddress("localhost", 8888)).sync();
        Agreement.MessageProtocol messageProtocol = new Agreement.MessageProtocol();
        messageProtocol.setType((byte) 'b');

        messageProtocol.setContent("hello world".getBytes("UTF-8"));
        for (int i = 0; i < 10; i++) {
            messageProtocol.setSequenceId(i);
            cf.channel().writeAndFlush(messageProtocol);
        }

        //cf.channel().close();
        //work.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        createClient3();
    }
}
