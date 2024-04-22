package org.test.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
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

    //客户端需要一个事件循环组
    private static EventLoopGroup group;

    static {
        group = new NioEventLoopGroup();
    }

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

    public static void main(String[] args) throws InterruptedException {

        createClient();
        //创建客户端启动对象
        //注意客户端使用的不是 ServerBootstrap 而是 Bootstrap
        //Bootstrap bootstrap = new Bootstrap();
        //try {
        //    //设置相关参数
        //    bootstrap
        //            .group(group) //设置线程组
        //            .channel(NioSocketChannel.class) //设置客户端通道的实现类(反射)
        //            .handler(new ChannelInitializer<SocketChannel>() { //设置处理器
        //                @Override
        //                protected void initChannel(SocketChannel ch) throws Exception {
        //                    ch.pipeline().addLast(new NettyClientHandler());
        //                }
        //            });
        //    //启动客户端去连接服务器端
        //    //关于 ChannelFuture 要分析，涉及到netty的异步模型
        //    ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
        //    //对关闭通道事件  进行监听
        //    channelFuture.channel().closeFuture().sync();
        //} catch (Exception e) {
        //    e.printStackTrace();
        //} finally {
        //    //关闭线程
        //    group.shutdownGracefully();
        //}
    }

    private static class NettyClientHandler extends ChannelInboundHandlerAdapter {

        //当通道就绪就会触发该方法
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("client " + ctx);
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server: (>^ω^<)喵", CharsetUtil.UTF_8));
        }

        //当通道有读取事件时，会触发
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            System.out.println("服务器回复的消息:" + buf.toString(CharsetUtil.UTF_8));
            System.out.println("服务器的地址： "+ ctx.channel().remoteAddress());
        }

        //通道异常时，触发该方法
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
