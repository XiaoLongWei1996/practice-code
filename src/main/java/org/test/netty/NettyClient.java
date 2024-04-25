package org.test.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @description: netty客户端
 * @Title: NettyClient
 * @Author xlw
 * @Package org.test.netty
 * @Date 2023/9/27 17:09
 */
public class NettyClient {

    /**
     * 客户端只有一个NioEventLoopGroup,死循环监听读事件
     * 创建客户端
     */
    public static void createClient() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup g = new NioEventLoopGroup(1);
        ChannelFuture cf = bootstrap
                .group(g) //设置NioEventLoopGroup
                .channel(NioSocketChannel.class) //设置客户端的管道类型
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 消息会经过通道 handler 处理，这里是将 String => ByteBuf 编码发出
                        ch.pipeline().addLast(new StringEncoder());
                        //ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                        //    @Override
                        //    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                        //        System.out.println("客户端收到消息:" + msg);
                        //    }
                        //});
                    }
                }) //设置NioSocketChannel处理器
                .connect(new InetSocketAddress("localhost", 8888)) //连接服务端
                .sync();//等待连接完成
        //获取管道
        Channel channel = cf.channel();
        channel.writeAndFlush("hello world");
    }

    public static void createClient1() {
        EventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture cf = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //字符串解码
                        ch.pipeline().addLast(new StringEncoder());

                        ch.pipeline().addLast(new NettyClientHandler());

                        //ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                        //    @Override
                        //    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                        //        System.out.println("客户端收到消息" + Thread.currentThread().getName() + ":" + msg);
                        //        ctx.writeAndFlush("thank you");
                        //    }
                        //});
                    }
                })
                .connect(new InetSocketAddress("localhost", 8888));
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("连接成功");
                } else {
                    System.out.println("连接失败");
                }
            }
        });
        //try {
        //    cf.channel().close().sync();
        //} catch (InterruptedException e) {
        //    throw new RuntimeException(e);
        //}
    }

    public static void createClient2() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup(1);
        EventLoopGroup extendGroup = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture cf = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //字符串编码,发送数据  String => ByteBuf 编码发出
                        ch.pipeline().addLast(new StringEncoder());
                        //字符串解码,接受数据  ByteBuf => String 解码接收
                        ch.pipeline().addLast(new StringDecoder()).addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                System.out.println("客户端收到消息" + Thread.currentThread().getName() + ":" + msg);
                                //ctx.writeAndFlush("hello");
                            }
                        });

                        ch.pipeline().addLast(new StringDecoder()).addLast(extendGroup, new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                System.out.println("客户端收到消息" + Thread.currentThread().getName() + ":" + msg);
                                ctx.writeAndFlush("thank you");
                            }
                        });
                    }
                })
                .connect(new InetSocketAddress("localhost", 8888)).sync();
        cf.channel().writeAndFlush("1");

    }

    /**
     * 沾包现象演示,发送10次数据,然而服务端却只接收一次,一次的大小是10次的总和
     * 发送方在发送数据时，并不是一条一条地发送数据，而是将数据整合在一起，当数据达到一定的数量后再一起发送。这就会导致多条信息被放在一个缓冲区中被一起发送出去
     */
    public static void createClient3() throws InterruptedException {
        EventLoopGroup work = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture cf = bootstrap
                .group(work)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                // 每次发送16个字节的数据，共发送10次
                                for (int i = 0; i < 10; i++) {
                                    ByteBuf buffer = ctx.alloc().buffer();
                                    buffer.writeBytes("sidiot.".getBytes());
                                    ctx.writeAndFlush(buffer);
                                }
                                //解决方式:使用短连接,每次发送完毕就断开连接
                                //ByteBuf buffer = ctx.alloc().buffer();
                                //buffer.writeBytes("sidiot.".getBytes());
                                //ctx.writeAndFlush(buffer);
                                //ctx.channel().close();

                            }
                        });
                    }
                })
                .connect("localhost", 8888).sync();
        work.shutdownGracefully();
        cf.channel().closeFuture().sync();
        //cf.channel().closeFuture().addListener(new ChannelFutureListener() {
        //    @Override
        //    public void operationComplete(ChannelFuture future) throws Exception {
        //        if (future.isSuccess()) {
        //            work.shutdownGracefully();
        //            System.out.println("关闭成功");
        //        }
        //    }
        //});
    }

    /**
     * 半包现象演示,发送一条数据,却被分割成几次发送
     * 接收方的缓冲区的大小是有限的，当接收方的缓冲区满了以后，就需要将信息截断，等缓冲区空了以后再继续放入数据。这就会发生一段完整的数据最后被截断的现象
     */
    public static void createClient4() throws InterruptedException {
        EventLoopGroup work = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture cf = bootstrap
                .group(work)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                ByteBuf buffer = ctx.alloc().buffer();
                                for (int i = 0; i < 10; i++) {
                                    buffer.writeBytes("sidiot.".getBytes());
                                }
                                ctx.writeAndFlush(buffer);
                                //我们可以获知原先的70字节的数据包被拆分成了两个数据包，其大小分别为14字节和56字节，也都恰好是7的倍数。
                            }
                        });
                    }
                })
                .connect("localhost", 8888).sync();
        cf.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {

        createClient3();

    }

    private static class NettyClientHandler extends ChannelInboundHandlerAdapter {

        //当通道就绪就会触发该方法
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server: (>^ω^<)喵", CharsetUtil.UTF_8));
        }

        //当通道有读取事件时，会触发
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            //System.out.println("客户端读");
            //System.out.println(msg);
            ByteBuf buf = (ByteBuf) msg;
            System.out.println("服务器回复的消息:" + buf.toString(CharsetUtil.UTF_8));
            //System.out.println("服务器的地址： " + ctx.channel().remoteAddress());
        }

        //通道异常时，触发该方法
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
