package com.test.springboot.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author 肖龙威
 * @date 2022/07/04 17:14
 */
@ServerEndpoint("/message/{token}") //每一次访问都会创建一个该类的实例(原型的)
@Component //默认是单例的
public class WebScoketServer {

    /**
     * 连接线程数,类变量,所有实例共享
     */
    private volatile static int threadCount = 0;

    /**
     * 保存每一个连接的map数组(实际为了保存session),并发访问用安全map,类变量,所有实例共享
     */
    private static ConcurrentHashMap<String, WebScoketServer> map = new ConcurrentHashMap();

    /**
     * 创建线程池,执行发送消息方法
     */
    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3,
            10,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(5),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    /**
     * websocket访问session对象
     */
    private Session session;

    /**
     * 唯一是识别符,识别session属于那个线程
     */
    private String token;

    /**
     * 建立连接,连接成功时候调用的方法
     *
     * @param token   唯一识别符,去路径变量
     * @param session 访问session对象
     */
    @OnOpen
    public void onOpen(@PathParam("token") String token, Session session) {
        this.token = token;
        this.session = session;
        if (!map.containsKey(token)) {
            //被map管理
            map.put(token, this);
            incrThreadCount();
        } else {
            //重新管理
            map.remove(token);
            map.put(token, this);
        }
        System.out.println(token + ":成功加入连接 <=====> " + threadCount);
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        if (map.containsKey(token)) {
            map.remove(token);
            decrThreadCount();
        }
        System.out.println(token + ":关闭连接 <=====> " + threadCount);
    }

    /**
     * 接收消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println(token + ":收到消息=====> " + message);
    }

    /**
     * 接收错误信息
     *
     * @param error
     * @param session
     */
    @OnError
    public void onError(Throwable error, Session session) {
        System.out.println(token + ":收到错误消息=====> " + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 服务器主动发送信息给客户端
     *
     * @param message
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送自定义信息,向外暴露的发送方法
     *
     * @param message
     * @param token
     */
    public static void sendMessage(String message, @PathParam("token") String token){
        threadPool.execute(() -> {
            if (map.containsKey(token)) {
                System.out.println("向" + token + "对象发送消息=====>" + message);
                try {
                    map.get(token).sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(token + ":用户不在线");
            }
        });
    }

    /**
     * 返回当前的连接数
     *
     * @return
     */
    public synchronized static int getThreadCount() {
        return threadCount;
    }

    /**
     * 增加线程数
     */
    private synchronized void incrThreadCount() {
        threadCount++;
    }

    /**
     * 减少线程数
     */
    private synchronized void decrThreadCount() {
        threadCount--;
    }

}
