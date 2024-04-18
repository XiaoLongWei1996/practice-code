package org.test.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @description: 自定义http协议
 * @Title: CustomHttp
 * @Author xlw
 * @Package org.test.socket
 * @Date 2024/4/17 16:13
 */
public class CustomHttp {

    //设置编码格式
    private String charSet = "UTF-8";

    //服务的socket
    private ServerSocketChannel server;

    //端口号
    private int port;

    /**
     * 选择器
     */
    private Selector selector;

    public CustomHttp(int port) {
        this.port = port;
    }

    public void initServer() {
        try {
            //创建服务端socket
            server = ServerSocketChannel.open();
            //绑定网络端口
            server.bind(new InetSocketAddress(port));
            //设置为非阻塞
            server.configureBlocking(false);
            //创建选择器
            selector = Selector.open();
            //注册接受请求事件
            server.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                //查询是否有事件
                int select = selector.select(2000);
                if (select == 0) {
                    //System.out.println("暂无可执行的事件通道");
                    continue;
                }
                System.out.println("有可执行的事件通道");
                //获取所有事件-管道绑定的key
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                //遍历所有事件通道
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    //获取具体的SelectionKey
                    SelectionKey sk = iterator.next();
                    if (sk.isAcceptable()) {
                        //连接事件
                        //获取客户端socket
                        SocketChannel clientSocket;
                        try {
                            clientSocket = server.accept();
                            if (clientSocket == null) {
                                return;
                            }
                            //设置非阻塞
                            clientSocket.configureBlocking(false);
                            //注册读事件
                            clientSocket.register(selector, SelectionKey.OP_READ);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (sk.isReadable()) {
                        //读事件
                        //从SelectionKey获取客户端的socket
                        SocketChannel clientSocket = (SocketChannel) sk.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(2048);
                        try {
                            clientSocket.read(buffer);
                            //将缓冲区切换读模式
                            buffer.flip();
                            //读取客户端的消息
                            String receive = Charset.forName(charSet).newDecoder().decode(buffer).toString();
                            System.out.println(receive);
                            String[] requestMessage = receive.split("\r\n");
                            //响应客户端
                            response(clientSocket, buffer, requestMessage);
                            //关闭客户端
                            clientSocket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //移除处理完的客户端socket
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void response(SocketChannel clientSocket, ByteBuffer buffer, String[] requestMessage) throws IOException {
        //返回客户端
        StringBuilder sendStr = new StringBuilder();
        sendStr.append("Http/1.1 200 Ok\r\n");
        sendStr.append("Content-Type:text/html;charset=" + charSet + "\r\n");
        sendStr.append("\r\n");
        sendStr.append("<html><head><title>显示报文</title></head><body>");
        sendStr.append("接受到请求的报文是：+<br>");
        for (String s : requestMessage) {
            sendStr.append(s + "<br/>");
        }
        sendStr.append("</body></html>");
        //切换成写模式
        buffer.clear();
        buffer = ByteBuffer.wrap(sendStr.toString().getBytes(charSet));
        clientSocket.write(buffer);
        clientSocket.finishConnect();
    }

    public static void main(String[] args) {
        CustomHttp customHttp = new CustomHttp(8888);
        customHttp.initServer();
    }

}
