package org.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 发布事务(发布确认):生产者的通道设置为confirm模式,所有在该通道发布的消息都会被指派一个唯一的ID
 * 当消息正确的到达mq队列或者写入磁盘后,会发送一条确认信息给生产者,生产者可以通过回调函数来处理该确
 * 认消息;如果mq内部缘故导致消息丢失,那么mq就会发送一条nack消息给生产者,生产者同样可以通过回调函数
 * 处理nack消息。
 * <p>
 * 1.单个发布确认:发布一条消息后调用channel.waitForConfirms()方法,会阻塞同步等待mq的确认信息;发布的速度很慢;
 * 2.批量发布确认:发布一批消息后调用channel.waitForConfirms()方法,会阻塞同步等待mq的确认信息;发布速度比单个提高,但是万一一批数据中某个消息出问题
 * 将不知是那一条消息出了问题;
 * 3.异步发布确认:创造者创建消息发送给mq,不需要同步等待mq发送确认信息,而是channel.addConfirmListener(confirm, nack)监听器,异步监听mq接收消息
 *              的成功或者失败;发布速度快。
 *
 * @author 肖龙威
 * @date 2022/07/12 16:43
 */
public class TransactionProducer {

    private static final String QUEUE_NAME = "durable_test";

    private static final int COUNT = 10;

    @SuppressWarnings("all")
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        asynConfirm();
    }

    //单个同步确认
    private static void singleSynConfirm() throws IOException, InterruptedException, TimeoutException {
        Channel channel = MQConnectionUtils.createChannel();
        //开始发布确认
        channel.confirmSelect();
        //channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            //需要发送到队列中的消息
            String message = scanner.next();
            if (message.equals("exit")) {
                break;
            }
            //发布消息
            channel.basicPublish(
                    "", //发送到那个交换机
                    QUEUE_NAME, //路由key(也就是队列名称)
                    MessageProperties.PERSISTENT_TEXT_PLAIN, //持久化消息
                    message.getBytes()  //发送的消息
            );
            boolean confirm = channel.waitForConfirms(); //阻塞等待mq返回的确认消息
            if (confirm) {
                System.out.println("消息发送成功");
            }
        }
        System.out.println("关闭");
        //关闭通道
        channel.close();
    }

    //批量同步确认
    private static void multipartSynConfirm() throws IOException, InterruptedException, TimeoutException {
        Channel channel = MQConnectionUtils.createChannel();
        //开始发布确认
        channel.confirmSelect();
        //channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        Scanner scanner = new Scanner(System.in);
        int num = 0;
        while (scanner.hasNext()) {
            //需要发送到队列中的消息
            String message = scanner.next();
            if (message.equals("exit")) {
                break;
            }
            num++;
            //发布消息
            channel.basicPublish(
                    "", //发送到那个交换机
                    QUEUE_NAME, //路由key(也就是队列名称)
                    MessageProperties.PERSISTENT_TEXT_PLAIN, //持久化消息
                    message.getBytes()  //发送的消息
            );
            if (num % 5 == 0) {
                boolean confirm = channel.waitForConfirms(); //阻塞等待mq返回的确认消息
                if (confirm) {
                    System.out.println("消息发送成功");
                    num = 0;
                }
            }
        }
        System.out.println("关闭");
        //关闭通道
        channel.close();
    }

    //异步监听确认
    private static void asynConfirm() throws IOException, TimeoutException {
        Channel channel = MQConnectionUtils.createChannel();
        //开启发布确认
        channel.confirmSelect();
        //线程安全有序的map,适用于高并发,用来记录发送的消息
        //1.轻松将消息序列号和消息内容进行关联
        //2.轻松删除条目,只要给序列号
        //3.支持并发访问
        //4.有序
        ConcurrentSkipListMap<Long, String> confirmMap = new ConcurrentSkipListMap<>();
        //mq成功收到消息的回调函数
        ConfirmCallback confirmCallback = (
                num          //如果是批量发送消息,最后一条消息序列号
                , multipart    //是否是批量的消息,true:是, false:否
        ) -> {
            //mq成功接收,删除map里面的记录
            if (multipart) {
                //返回confirmMap中key小于等于num的部门视图,对视图操作会反应到集合上
                ConcurrentNavigableMap<Long, String> longStringConcurrentNavigableMap = confirmMap.headMap(num, true);
                longStringConcurrentNavigableMap.clear();
            } else {
                //单个发送的消息
                confirmMap.remove(num);
            }
        };
        //mq接收失败的回调函数
        ConfirmCallback nackCallback = (num, multipart) -> {
            String message = confirmMap.get(num);
            System.out.println(message + ":未被确认");
        };
        //添加异步信息确认监听器
        channel.addConfirmListener(
                confirmCallback  //成功确认的监听器
                , nackCallback   //失败监听器
        );
        //批量发送消息
        for (int i = 0; i < COUNT; i++) {
            //消息
            String message = "消息" + i;
            //获取下一个信息的序列号
            long num = channel.getNextPublishSeqNo();
            //将系列号跟信息进行绑定
            confirmMap.put(num, message);
            //推送消息
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        }
        channel.close();
    }
}
