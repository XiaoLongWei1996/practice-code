package org.test.channel;

import sun.security.util.CurveDB;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author 肖龙威
 * @date 2022/02/28 16:15
 */
public class TestChinnel {

    private static int num = 1;

    static{
        num = 2;
        number = 20;
        //System.out.println(num);
        //System.out.println(number);//报错：非法的前向引用。
    }

    /**
     * 1、linking之prepare: number = 0 --> initial: 20 --> 10
     * 2、这里因为静态代码块出现在声明变量语句前面，所以之前被准备阶段为0的number变量会
     * 首先被初始化为20，再接着被初始化成10（这也是面试时常考的问题哦）
     *
     */
    private static int number = 10;

    public static void main(String[] args) {
        ClassLoader classLoader1 = CurveDB.class.getClassLoader();
        System.out.println(classLoader1);
    }

    private static void testPipeChinel() {
        try {
            Pipe pipes = Pipe.open();
            Pipe.SinkChannel sink = pipes.sink();
            Pipe.SourceChannel source = pipes.source();

            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + ":写");
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.put("大家好".getBytes());
                buffer.flip();
                try {
                    sink.write(buffer);
                    sink.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, "A").start();

            new Thread(() -> {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                try {
                    source.read(buffer);
                    buffer.flip();
                    System.out.println(Thread.currentThread().getName() + ":读" + new String(buffer.array(), 0, buffer.limit()));
                    source.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            },"B").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testPipe() {
        PipedInputStream input = new PipedInputStream();
        PipedOutputStream output = new PipedOutputStream();
        try {
            input.connect(output);
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + ":写");
                    output.write("你好啊".getBytes());
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, "A").start();

            new Thread(() -> {
                byte[] data = new byte[1024];
                try {
                    int count = input.read(data);
                    System.out.println(Thread.currentThread().getName() + ":读到" + new String(data, 0, count));
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, "B").start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testChanel01() {
        try {
            FileInputStream inputStream = new FileInputStream("D:\\a.jpeg");
            FileChannel in = inputStream.getChannel();
            FileOutputStream outputStream = new FileOutputStream("D:\\b.jpeg");
            FileChannel out = outputStream.getChannel();
            out.force(true);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (in.read(buffer) != -1){
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
            in.close();
            out.close();
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testChanel02() {
        try {
            FileInputStream inputStream = new FileInputStream("D:\\a.jpeg");
            FileChannel in = inputStream.getChannel();
            FileOutputStream outputStream = new FileOutputStream("D:\\b.jpeg");
            FileChannel out = outputStream.getChannel();

            ByteBuffer buffer01 = ByteBuffer.allocate(1024);
            ByteBuffer buffer02 = ByteBuffer.allocate(1024);
            ByteBuffer[] buffers = {buffer01, buffer02};
            while (in.read(buffers) != -1){ //分散读
                for (ByteBuffer buffer : buffers) {
                    buffer.flip();
                }
                out.write(buffers);  //聚合写
                for (ByteBuffer buffer : buffers) {
                    buffer.clear();
                }
            }
            in.close();
            out.close();
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //零拷贝
    private static void testChanel03() {
        try {
            FileInputStream inputStream = new FileInputStream("D:\\a.jpeg");
            FileChannel in = inputStream.getChannel();
            FileOutputStream outputStream = new FileOutputStream("D:\\b.jpeg");
            FileChannel out = outputStream.getChannel();
            in.transferTo(0,in.size(), out);
            in.close();
            out.close();
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //使用直接缓冲区完成文件的复制(内存映射文件)，零拷贝
    private static void testChanel04() {
        try {
            FileInputStream inputStream = new FileInputStream("D:\\a.jpeg");
            FileChannel in = inputStream.getChannel();
            FileChannel out = FileChannel.open(Paths.get("D:\\b.jpeg"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
            //内存映射文件
            MappedByteBuffer inMap = in.map(FileChannel.MapMode.READ_ONLY, 0, in.size());
            MappedByteBuffer outMap = out.map(FileChannel.MapMode.READ_WRITE, 0, in.size());
//            byte[] b = new byte[inMap.limit()];
//            inMap.get(b);
//            outMap.put(b);
            outMap.put(inMap.asReadOnlyBuffer());
            in.close();
            out.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
