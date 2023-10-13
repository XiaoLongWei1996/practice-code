package org.test.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @description: netty服务端
 * @Title: NettyServer
 * @Author xlw
 * @Package org.test.netty
 * @Date 2023/9/27 16:38
 */
public class NettyServer {

    //bossGroup线程专门处理客户端连接，客户端的业务处理交给workerGroup线程
    private static EventLoopGroup bossGroup;

    //workerGroup线程用来处理客户端的业务
    private static EventLoopGroup workerGroup;

    //服务端启动对象
    private static ServerBootstrap serverBootstrap;

    static {

    }

    public static void main(String[] args) {

        try {
            //创建bossGroup
            bossGroup = new NioEventLoopGroup(1);
            //创建workerGroup
            workerGroup = new NioEventLoopGroup(2);
            //创建服务端启动对象
            serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup, workerGroup)          //设置两个线程对象
                    .channel(NioServerSocketChannel.class)  //设置管道对象，用来数据传输的
                    .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列等待时间
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道初始化对象
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //给管道设置处理器
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            System.out.println(".....服务器 is ready...");

            //绑定一个端口并且同步生成了一个 ChannelFuture 对象（也就是立马返回这样一个对象）
            //启动服务器(并绑定端口)
            ChannelFuture cf = serverBootstrap.bind(6668).sync();
            //给cf 注册监听器，监控我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (cf.isSuccess()) {
                        System.out.println("监听端口 6668 成功");
                    } else {
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });

            //对关闭通道事件  进行监听
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭线程
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    private static class NettyServerHandler extends ChannelInboundHandlerAdapter {

        //通道就绪触发该方法
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("client: " + ctx);
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server: (>^ω^<)喵", CharsetUtil.UTF_8));
        }

        //读取数据事件(这里我们可以读取客户端发送的消息)
        /*
            1. ChannelHandlerContext ctx:上下文对象, 含有 管道pipeline , 通道channel, 地址
            2. Object msg: 就是客户端发送的数据 默认Object
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 比如这里我们有一个非常耗时长的业务-> 异步执行 -> 提交该channel 对应的
            // NIOEventLoop 的 taskQueue中,

            // 解决方案1 异步发送
            ctx.channel().eventLoop().execute(() -> {
                ctx.writeAndFlush(Unpooled.copiedBuffer("你好", CharsetUtil.UTF_8));
            });
            //延迟发送
            ctx.channel().eventLoop().schedule(() -> {
                ctx.writeAndFlush(Unpooled.copiedBuffer("你好", CharsetUtil.UTF_8));
            }, 3, TimeUnit.SECONDS);
//            ByteBuf buf = (ByteBuf) msg;
//            System.out.println("服务器回复的消息:" + buf.toString(CharsetUtil.UTF_8));
//            System.out.println("服务器的地址： "+ ctx.channel().remoteAddress());
        }

        //发生异常后, 一般是需要关闭通道
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }

}
