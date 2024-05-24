package org.test.netty;

import io.fury.Fury;
import io.fury.config.Language;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;
import org.test.util.SerializeUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;

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

    public static void httpServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup(2);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 作为服务器，使用 HttpServerCodec 作为编码器与解码器
                        ch.pipeline().addLast(new HttpServerCodec());
                        // 服务器只处理HTTPRequest
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
                                System.out.println("请求链接:" + msg.uri());
                                //设置响应内容
                                DefaultFullHttpResponse response = new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);
                                // 设置响应内容
                                byte[] bytes = "<h1>Hello, World!</h1>".getBytes(StandardCharsets.UTF_8);
                                // 设置响应体长度，避免浏览器一直接收响应内容
                                response.headers().setInt("CONTENT_LENGTH", bytes.length);
                                // 设置响应体
                                response.content().writeBytes(bytes);
                                // 写回响应
                                ctx.writeAndFlush(response);
                                //关闭客户端链接，一次访问
                                ctx.channel().close();
                            }
                        });
                    }
                })
                .bind(8888);
    }

    public final static MyMessageCodec MY_MESSAGE_CODEC = new MyMessageCodec();

    /**
     * 自定义协议组成要素
     * 魔数：用来在第一时间判定接收的数据是否为无效数据包
     * 版本号：可以支持协议的升级
     * 序列化算法：消息正文到底采用哪种序列化反序列化方式
     * 如：json、protobuf、hessian、jdk
     * 指令类型：是登录、注册、单聊、群聊… 跟业务相关
     * 请求序号：为了双工通信，提供异步能力
     * 正文长度
     * 消息正文
     */
    public static void definedAgreement() throws InterruptedException {

        EventLoopGroup boss = new NioEventLoopGroup(1);

        EventLoopGroup work = new NioEventLoopGroup(2);

        ServerBootstrap server = new ServerBootstrap();
        server
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //设置自定义解码编码器
                        ch.pipeline().addLast(MY_MESSAGE_CODEC);
                        //设置入站
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<MessageProtocol>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
                                System.out.println("服务端收到数据:" + msg.getSequenceId() + ":" + new String(msg.getContent(), "UTF-8"));
                                MessageProtocol reply = new MessageProtocol();
                                reply.setType((byte) 2);
                                reply.setSequenceId(456);
                                reply.setContent("谢谢".getBytes("UTF-8"));
                                ctx.writeAndFlush(msg);
                            }
                        });
                    }
                })
                .bind(8888).sync();

    }

    /**
     * 使用自定义协议+编解码器来解决沾包
     * 自定义编解码器
     * 入站消息会被解码：从字节转换为另一种格式（比如 java 对象）；如果是出站消息，它会被编码成字节。
     * Netty 提供一系列实用的编解码器，他们都实现了 ChannelInboundHadnler 或者 ChannelOutboundHandler 接口。
     * 在这些类中，channelRead 方法已经被重写了。以入站为例，对于每个从入站 Channel 读取的消息，这个方法会被调用。
     * 随后，它将调用由解码器所提供的 decode() 方法进行解码，并将已经解码的字节转发给 ChannelPipeline 中的下一个 ChannelInboundHandler。
     *
     * @author xlw
     * @date 2024/04/28
     */
    @ChannelHandler.Sharable
    public static class MyMessageCodec extends ByteToMessageCodec<MessageProtocol> {

        private Fury fury;

        private static byte[] MAGIC = {'N', 'Y', 'I', 'M'};

        public MyMessageCodec() {
            System.out.println("创建编码器");
            fury = Fury
                    .builder()
                    .withLanguage(Language.JAVA)
                    .build();
        }

        /**
         * 编码,将出站的对象MessageProtocol转换成ByteBuf
         *
         * @param ctx ctx
         * @param msg 味精
         * @param out 出
         * @throws Exception 异常
         */
        @Override
        protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
            System.out.println("编码");
            //设置魔数,4个字节
            out.writeBytes(MAGIC);
            //设置协议版本号
            out.writeByte(1);
            //设置序列化算法
            out.writeByte(1);
            //设置指令类型1个字节
            out.writeByte(msg.getType());
            //设置请求序列号4个字节
            out.writeInt(msg.getSequenceId());
            //补齐12个字节
            out.writeByte(0xff);
            //序列化
            byte[] data = SerializeUtil.serialize(msg);
            //设置消息正文
            out.writeInt(data.length);
            msg.setLength(data.length);
            //设置序列化
            out.writeBytes(data);
        }

        /**
         * 解码,将入站的数据ByteBuf转换成MessageProtocol并传递给下一个handler
         *
         * @param ctx ctx
         * @param in  在
         * @param out 出
         * @throws Exception 异常
         */
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            System.out.println("解码");
            //获取魔数
            int magic = in.readInt();
            //获取版本号
            byte version = in.readByte();
            //获取序列化算法
            byte seqType = in.readByte();
            //获取指令类型
            byte type = in.readByte();
            //获取请求序列号
            int sequenceId = in.readInt();
            //获取补齐字节
            byte b = in.readByte();
            //获取正文长度
            int length = in.readInt();
            //获取正文
            byte[] data = new byte[length];
            in.readBytes(data, 0, length);
            //反序列化
            MessageProtocol messageProtocol = SerializeUtil.deserialize(data, MessageProtocol.class);
            //传递给下一个handler
            out.add(messageProtocol);

            //System.out.println("魔数:" + magic);
            //
            //System.out.println("版本号:" + version);
            //
            //System.out.println("序列化算法:" + seqType);
            //
            //System.out.println("指令类型:" + type);
            //
            //System.out.println("请求序列号" + sequenceId);
        }

    }

    @Data
    public static class MessageProtocol {

        /**
         * 内容
         */
        private byte[] content;

        /**
         * 内容的长度,4个字节32位
         */
        private int length;

        /**
         * 类型,1个字节
         */
        private byte type;

        /**
         * 请求序列id,4个字节
         */
        private int sequenceId;

    }

    public static void main(String[] args) {
        try {
            definedAgreement();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
