package testspringboot.testspringboot.websocket;

import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * websocket客户端,后台连接websocket
 * @author 肖龙威
 * @date 2022/07/22 8:50
 */
public class MyWebSocketClient implements WebSocketHandler {
    public static void main(String[] args) throws URISyntaxException {
        StandardWebSocketClient client = new StandardWebSocketClient();
        client.doHandshake(new MyWebSocketClient(), new WebSocketHttpHeaders(), new URI("ws://localhost:9091/message/0001"));
        client.setTaskExecutor(new ConcurrentTaskExecutor());
        Scanner scanner = new Scanner(System.in);
        scanner.next();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("建立连接");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("接收到信息:" + message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("错误信息");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("关闭连接");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
