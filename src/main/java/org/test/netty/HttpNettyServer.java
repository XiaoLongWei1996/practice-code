package org.test.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @description: http服务端
 * @Title: HttpNettyServer
 * @Author xlw
 * @Package org.test.netty
 * @Date 2023/9/28 18:00
 */
public class HttpNettyServer {

    private static EventLoopGroup bossGroup;

    private static EventLoopGroup workerGroup;

    static {
        bossGroup = new NioEventLoopGroup(1);

        workerGroup = new NioEventLoopGroup(2);
    }

    public static void main(String[] args) {
        ServerBootstrap bootstrap = null;
        try {
            bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            //向管道加入处理器
                            //得到管道
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入一个netty 提供的httpServerCodec codec =>[coder - decoder]
                            //HttpServerCodec 说明
                            //1. HttpServerCodec 是netty 提供的处理http的 编-解码器
                            pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
                            //2. 增加一个自定义的handler
                            pipeline.addLast("MyTestHttpServerHandler", new HttpServerHandler());
                            System.out.println("ok~~~~");
                        }
                    });
            ChannelFuture sync = bootstrap.bind(6666).sync();
            sync.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("6666端口绑定完成");
                    } else {
                        System.out.println("6666端口绑定失败");
                    }
                }
            });

            sync.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private static class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

        //channelRead0 读取客户端数据
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
            System.out.println("对应的channel=" + ctx.channel() + " pipeline=" + ctx.pipeline() + " 通过pipeline获取channel" + ctx.pipeline().channel());
            System.out.println("当前ctx的handler=" + ctx.handler());

            if (msg instanceof HttpObject) {
                System.out.println("ctx 类型="+ctx.getClass());

                System.out.println("pipeline hashcode" + ctx.pipeline().hashCode() + " TestHttpServerHandler hash=" + this.hashCode());

                System.out.println("msg 类型=" + msg.getClass());
                System.out.println("客户端地址" + ctx.channel().remoteAddress());

                HttpRequest hr = (HttpRequest) msg;
                URI uri = new URI(hr.uri());
                System.out.println(uri.getPath());
                //回复信息给浏览器 [http协议]
                ByteBuf content = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8);

                //构造一个http的相应，即 httpresponse
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

                //将构建好 response返回
                ctx.writeAndFlush(response);
            }
        }
    }
}
