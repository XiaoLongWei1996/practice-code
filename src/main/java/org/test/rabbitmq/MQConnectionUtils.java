package org.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 肖龙威
 * @date 2022/07/11 13:59
 */
public class MQConnectionUtils {

    public static Channel createChannel() {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("www.xiaolongwei.cn");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("x123456");
        //创建连接
        Connection connection = null;
        //创建管道
        Channel channel = null;
        try {
             connection = factory.newConnection();
             channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
