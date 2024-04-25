package org.test.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @description: netty服务端
 * @Title: NettyServer
 * @Author xlw
 * @Package org.test.netty
 * @Date 2023/9/27 16:38
 */
public class NettyServer {

    /**
     * 单NioEventLoopGroup，一个NioEventLoopGroup里面可以有多个NioEventLoop;每个NioEventLoop都有一个Selector,NioEventLoop死循环监听Selector上绑定的事件(连接/读/写)
     * <p>
     * 创建服务器
     */
    public static void createServer() {
        ServerBootstrap server = new ServerBootstrap();
        //创建nio的事件组,指定为两个线程,也就是拥有两个NioEventLoop
        NioEventLoopGroup group = new NioEventLoopGroup(2);
        server
                .group(group)  //设置事件组
                .channel(NioServerSocketChannel.class)  //设置管道类型
                .handler(new LoggingHandler(LogLevel.INFO))  //设置服务的NioServerSocketChannel处理器
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //pipeline管道,就是对NioSocketChannel的数据进行一系列的处理
                        //SocketChannel的处理器，使用StringDecoder解码，ByteBuf=>String
                        ch.pipeline().addLast(new StringDecoder());
                        //添加读请求处理
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                System.out.println("服务端收到消息:" + msg);
                            }
                        });
                    }
                })   //设置客户端连接的NioSocketChannel处理器
                .bind(8888); //设置端口号
    }

    /**
     * 服务端两个NioEventLoopGroup,Boss NioEventLoopGroup专门用来处理接受事件,work NioEventLoopGroup专门用来处理读/写事件
     * 创建server1
     */
    public static void createServer1() {
        //bossGroup线程专门处理客户端连接，客户端的业务处理交给workerGroup线程
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //workerGroup线程用来处理客户端的业务
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);
        //服务端启动对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup) //设置bossGroup和workerGroup组
                .channel(NioServerSocketChannel.class) //设置服务端管道类型
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //pipeline管道,就是对NioSocketChannel的数据进行一系列的处理
                        //SocketChannel的处理器，使用StringDecoder解码，ByteBuf=>String
                        ch.pipeline().addLast(new StringDecoder());
                        //添加读请求处理
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                System.out.println("服务端收到消息" + Thread.currentThread().getName() + ":" + msg);
                                ctx.writeAndFlush(Unpooled.copiedBuffer("hello, client: (>^ω^<)喵", CharsetUtil.UTF_8));
                            }
                        });
                    }
                }).bind(8888);
    }

    /**
     * EventLoopGroup可以理解为线程池+LoopGroup,EventLoopGroup是线程池,每一个线程对应一个EventLoop(单线程池);
     * EventLoop是单线程的(线程安全的),它会循环监控客户端的事件,并进行处理
     * 创建server2
     */
    public static void createServer2() {
        //bossGroup线程专门处理客户端连接，客户端的业务处理交给workerGroup线程
        EventLoopGroup bootGroup = new NioEventLoopGroup(1);
        //workerGroup线程用来处理客户端的业务
        EventLoopGroup workGroup = new NioEventLoopGroup(1);

        //拓展EventLoopGroup,workGroup处理完成后调用ctx.fireChannelRead(msg)交给extendGroup来执行
        EventLoopGroup extendGroup = new DefaultEventLoopGroup(1);

        ServerBootstrap server = new ServerBootstrap();
        ChannelFuture cf = server
                .group(bootGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder()).addLast(new StringEncoder());
                        ch.pipeline().addLast("defaultGroup", new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                if (msg.equals("1")) {
                                    System.out.println(Thread.currentThread().getName() + "处理:" + msg);
                                    Thread.sleep(5000);
                                    ctx.writeAndFlush("服务器收到" + msg);
                                } else {
                                    // 调用下一个handler,没有这个则不会执行extendGroup绑定的handler
                                    ctx.fireChannelRead(msg);
                                }

                                //延迟5秒发送数据
                                //ctx.channel().eventLoop().schedule(() -> {
                                //    ctx.writeAndFlush("服务器收到");
                                //}, 5, TimeUnit.SECONDS);
                            }
                        });
                        //ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                        //    @Override
                        //    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                        //        System.out.println(Thread.currentThread().getName() + "帮助处理:" + msg);
                        //        ctx.channel().writeAndFlush("服务器收到2");
                        //    }
                        //});

                        ch.pipeline().addLast(extendGroup, "extendGroup", new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

                                System.out.println(Thread.currentThread().getName() + "帮助处理:" + msg);
                                ctx.channel().writeAndFlush("服务器收到" + msg);
                            }
                        });
                    }
                })
                .bind(8888);
        cf.addListeners(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("服务端成功启动");
                } else {
                    System.out.println("服务端启动失败");
                }
            }
        });
    }

    /**
     * ChannelPipeline内部维系的一个双向链表连接ChannelHandler,读操作只会被ChannelInboundHandlerAdapter处理,处理顺序从head -> tail;
     * 写操作只会被ChannelOutboundHandlerAdapter处理,顺序是从tail -> head;
     * ctx.writeAndFlush()只会从当前handler向前寻找OutboundHandler;
     * ctx.channel().writeAndFlush()才会从tail -> head 寻找OutboundHandler;
     * 创建服务器3
     */
    public static void createServer3() {
        //bossGroup线程专门处理客户端连接，客户端的业务处理交给workerGroup线程
        EventLoopGroup bootGroup = new NioEventLoopGroup(1);
        //workerGroup线程用来处理客户端的业务
        EventLoopGroup workGroup = new NioEventLoopGroup(1);

        ServerBootstrap server = new ServerBootstrap();
        ChannelFuture cf = server
                .group(bootGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //ch.pipeline().addLast(new StringDecoder()).addLast(new StringEncoder());
                        //添加入站处理器
                        ch.pipeline().addLast("read1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //创建直接缓冲区
                                ByteBuf byteBuf = (ByteBuf) msg;
                                System.out.println(byteBuf);
                                System.out.println("入站1" + byteBuf.toString(Charset.forName("UTF-8")));
                                ctx.channel().writeAndFlush("你好");
                                // 将数据传递给下一个handler
                                super.channelRead(ctx, msg);
                            }
                        });
                        //添加入站处理器2
                        ch.pipeline().addLast("read2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                System.out.println(byteBuf);
                                System.out.println("入站2" + byteBuf.toString(Charset.forName("UTF-8")));
                                // 将数据传递给下一个handler
                                super.channelRead(ctx, msg);
                            }
                        });
                        //添加出站处理器1
                        ch.pipeline().addLast("write1", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                System.out.println("出站1" + msg);
                                super.write(ctx, msg, promise);
                            }
                        });

                        //添加出站处理器2
                        ch.pipeline().addLast("write2", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                System.out.println("出站2" + msg);
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                })
                .bind(8888);
        cf.addListeners(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("服务端成功启动");
                } else {
                    System.out.println("服务端启动失败");
                }
            }
        });
    }

    /**
     * ByteBuf是承载数据的对象类似nio中的ByteBUffer,不同的是不需要flip转换写模式,ByteBuf有读和写两个指针,并且会进行扩容操作;
     * ByteBuf分为池化和非池化两种,池化则是该ByteBuf能被复用,非池化不能被复用;
     * ByteBuf回收使用的是引用计数的方法,retain()会计数+1,release()会计数-1,当计数为0时,表示已经不被使用了,可以进行回收.
     * buffertest
     */
    public static void buffertest() {
        //创建一个可扩容的ByteBuf(非池化)
        ByteBuf buffer = Unpooled.buffer(1024);
        //创建一个固定大小的ByteBuf(非池化)
        ByteBuf buffer1 = Unpooled.buffer(1024, 2048);
        //创建一个可扩容的ByteBuf(池化)
        ByteBuf buffer2 = ByteBufAllocator.DEFAULT.buffer(1024);
        //创建一个固定大小的ByteBuf(池化)
        ByteBuf buffer3 = ByteBufAllocator.DEFAULT.buffer(1024, 2048);
        //创建一个直接缓冲区
        ByteBuf buffer4 = ByteBufAllocator.DEFAULT.directBuffer(1024);

        //写方法
        buffer.writeByte(1);
        buffer.writeByte(2);

        //保留,不被jvm回收
        buffer.retain();

        //用完得释放掉
        buffer.release();
    }

    /**
     * 沾包服务端
     * 粘包现象发生是因为发送方将两个或多个数据包连续地发送到网络中，而接收方一次性读取了多个数据包，从而把它们看作一个数据包处理，造成了粘包的现象。
     * 创建server4
     *
     * @throws InterruptedException 中断异常
     */
    public static void createServer4() throws InterruptedException {
        //boss组
        EventLoopGroup boss = new NioEventLoopGroup(1);
        //work组
        EventLoopGroup work = new NioEventLoopGroup(2);

        ServerBootstrap server = new ServerBootstrap();
        ChannelFuture cf = server
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        //设置定长解码器解决沾包问题,定长解码器指的是客户端和服务器之间进行数据传输时，双方事先约定一个最大长度。
                        //注意如果客户端发送的数据不足16,那么会向下一次请求数据进行补齐,会造成数据错乱
                        //ch.pipeline().addLast(new FixedLengthFrameDecoder(16));
                        //行解码器解决沾包问题,通过\n来对数据进行拆分
                        //ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        //限定符解码器,指定限定符来解决沾包
                        //ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, ch.alloc().buffer().writeBytes("\\s".getBytes())));
                        //长度字段解码器,指定数据的长度和数据
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 1, 4, 1, 0));
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("已连接" + ctx.channel());
                                super.channelActive(ctx);
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                NettyUtil.printByteBuf(byteBuf);
                                super.channelRead(ctx, msg);
                            }
                        });
                    }
                })
                .bind(8888);
        //cf.channel().closeFuture().sync();
        ChannelFuture closeCf = cf.channel().closeFuture();
        closeCf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("服务端成功关闭");
                }
            }
        });
    }

    /**
     * 半包服务端
     * 半包现象则是指发送方将一个数据包分割成多个数据块进行传输，在接收方接收到部分数据块时就开始处理数据，从而只处理了部分数据信息，无法还原完整的数据包。
     * 创建server4
     *
     * @throws InterruptedException 中断异常
     */
    public static void createServer5() throws InterruptedException {
        //boss组
        EventLoopGroup boss = new NioEventLoopGroup(1);
        //work组
        EventLoopGroup work = new NioEventLoopGroup(2);

        ServerBootstrap server = new ServerBootstrap();
        ChannelFuture cf = server
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_RCVBUF, 7) //设置服务端一次接收缓冲区的最小的字节,netty每次读取都会是7的整数倍
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("已连接" + ctx.channel());
                                super.channelActive(ctx);
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                NettyUtil.printByteBuf(byteBuf);
                                super.channelRead(ctx, msg);
                            }
                        });
                    }
                })
                .bind(8888);
        //cf.channel().closeFuture().sync();
        ChannelFuture closeCf = cf.channel().closeFuture();
        closeCf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("服务端成功关闭");
                }
            }
        });
    }



    public static void main(String[] args) throws InterruptedException {
        createServer4();
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

            System.out.println(msg);

            //// 解决方案1 异步发送
            //ctx.channel().eventLoop().execute(() -> {
            //    ctx.writeAndFlush(Unpooled.copiedBuffer("你好", CharsetUtil.UTF_8));
            //});
            ////延迟发送
            //ctx.channel().eventLoop().schedule(() -> {
            //    ctx.writeAndFlush(Unpooled.copiedBuffer("你好", CharsetUtil.UTF_8));
            //}, 3, TimeUnit.SECONDS);
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
