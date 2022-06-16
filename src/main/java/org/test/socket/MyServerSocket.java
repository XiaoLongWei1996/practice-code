package org.test.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 肖龙威
 * @date 2022/03/01 15:42
 */
public class MyServerSocket {

    public static void main(String[] args) {
        server05();
    }

    private static void server01() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            serverSocket = new ServerSocket(9999);
            while (true) {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                System.out.println(reader.readLine());
                if (inputStream != null) {
                    inputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //阻塞式的
    private static void server02() {
        ServerSocketChannel serverSocketChannel = null;
        SocketChannel socketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(9998));
            while (true) {
                socketChannel = serverSocketChannel.accept();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                socketChannel.read(buffer);
                buffer.flip();
                byte[] data = new byte[buffer.limit()];
                buffer.get(data);
                System.out.println(new String(data));
                socketChannel.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocketChannel != null) {
                    serverSocketChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //非阻塞式的
    private static void server03() {
        ServerSocketChannel serverSocketChannel = null;
        SocketChannel socketChannel = null;
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(9998));
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (selector.select() > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        buffer.clear();
                        socketChannel = (SocketChannel) selectionKey.channel();
                        socketChannel.read(buffer);
                        buffer.flip();
                        byte[] data = new byte[buffer.limit()];
                        buffer.get(data);
                        System.out.println(new String(data));
                        socketChannel.register(selector, SelectionKey.OP_WRITE);
                    } else if (selectionKey.isWritable()) {
                        buffer.clear();
                        socketChannel = (SocketChannel) selectionKey.channel();
                        buffer.put("服务器已收到信息".getBytes());
                        buffer.flip();
                        socketChannel.write(buffer);
                    }
                    //socketChannel.close();
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocketChannel != null) {
                    serverSocketChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void server04() {
        try {
            DatagramSocket receive = new DatagramSocket(8888);
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            receive.receive(packet);
            data = packet.getData();
            int length = packet.getLength();
            System.out.println(new String(data,0, length));
            receive.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void server05() {
        try {
           DatagramChannel receive = DatagramChannel.open();
           receive.bind(new InetSocketAddress(8888));
           receive.configureBlocking(false);
           Selector selector = Selector.open();
           receive.register(selector, SelectionKey.OP_READ);
           while (selector.select() > 0) {
               Set<SelectionKey> selectionKeys = selector.selectedKeys();
               Iterator<SelectionKey> iterator = selectionKeys.iterator();
               while (iterator.hasNext()) {
                   SelectionKey sk = iterator.next();
                   if (sk.isReadable()) {
                       ByteBuffer buffer = ByteBuffer.allocate(1024);
                       receive.receive(buffer);
                       buffer.flip();
                       System.out.println(new String(buffer.array(),0, buffer.limit()));
                   }
                   iterator.remove();
               }
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
