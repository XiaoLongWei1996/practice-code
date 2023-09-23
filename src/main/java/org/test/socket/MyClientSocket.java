package org.test.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

/**
 * @author 肖龙威
 * @date 2022/03/01 15:42
 */
public class MyClientSocket {

    public static void main(String[] args) {
        client01();
    }

    private static void client01() {
        Socket socket = null;
        OutputStream outputStream = null;
        PrintWriter printWriter = null;
        try {
            socket = new Socket("127.0.0.1", 9999);
            outputStream = socket.getOutputStream();
            printWriter = new PrintWriter(outputStream);
            //阻塞写
            printWriter.print("你好啊");
            printWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(outputStream != null) {
                    outputStream.close();
                }
                if (socket != null) {
                    socket.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void client02() {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9998));
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put("你好啊".getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
            System.out.println("客户端响应:" + new String());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socketChannel != null) {
                    socketChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void client03() {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9998));
            //socketChannel.configureBlocking(false);
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put("你好啊".getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
            byte[] data = new byte[byteBuffer.limit()];
            byteBuffer.get(data);
            System.out.println("客户端接收:" + new String(data));
            byteBuffer.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            try {
//                if (socketChannel != null) {
//                    socketChannel.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private static void client04() {
        try {
            DatagramSocket send = new DatagramSocket(9999);
            DatagramPacket packet = new DatagramPacket("你好啊".getBytes(), "你好啊".getBytes().length,new InetSocketAddress("127.0.0.1", 8888));
            send.send(packet);
            send.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void client05() {
        try {
            DatagramChannel send = DatagramChannel.open();
            send.bind(new InetSocketAddress(9999));
            send.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("你好 hello".getBytes());
            buffer.flip();
            send.send(buffer, new InetSocketAddress("127.0.0.1", 8888));
            send.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
