package org.test.socket;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 肖龙威
 * @date 2022/03/01 15:42
 */
public class MyServerSocket {

    public static void main(String[] args) {
        server06();
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
                //阻塞读
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
            //设置为nio
            serverSocketChannel.configureBlocking(false);
            //开启selector
            Selector selector = Selector.open();
            //将serverSocketChannel和SelectionKey.OP_ACCEPT绑定到selector
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            //selector.select()阻塞的
            while (selector.select() > 0) {
                //获取触发事件的SelectionKey集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        //获取客户端连接
                        socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        //注册客户端读事件连接
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        buffer.clear();
                        socketChannel = (SocketChannel) selectionKey.channel();
                        socketChannel.read(buffer);
                        buffer.flip();
                        byte[] data = new byte[buffer.limit()];
                        buffer.get(data);
                        System.out.println(new String(data));
                        //注册客户端写事件连接
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

    public static void server06() {
        ExecutorService poll = Executors.newFixedThreadPool(4);
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(9999);
            while (true) {
                //阻塞等待客户端的连接
                Socket s = ss.accept();
                //多线程处理客户端的连接
                poll.execute(() -> {
                    handler(s);
                });
                System.out.println("连接客户端:" + s.getInetAddress().getCanonicalHostName());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void handler(Socket s) {
        InputStream in = null;
        BufferedReader reader = null;
        try {
            in = s.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(Thread.currentThread().getName() + "" + line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
