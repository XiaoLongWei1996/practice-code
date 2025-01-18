package com.xlw.spring_design_demo.sse;


import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: sse客户端
 * @Title: SseClient
 * @Author xlw
 * @Package com.xlw.spring_design_demo.sse
 * @Date 2025/1/18 16:33
 */
public class SseClient {

    /**
     * SSE 池
     */
    public final static ConcurrentHashMap<String, SseEmitter> SSE_POOL = new ConcurrentHashMap<>();

    public static SseEmitter createSse(String key) {
        if (SSE_POOL.containsKey(key)) {
            return null;
        }
        //默认30秒超时,设置为0L则永不超时
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onCompletion(() -> {
            System.out.println("完成数据发送");
            SSE_POOL.remove(key);
        });

        sseEmitter.onTimeout(() -> {
            System.out.println("超时");
            SSE_POOL.remove(key);
        });

        sseEmitter.onError(e -> {
            System.out.println("发生错误");
            SSE_POOL.remove(key);
        });
        try {
            sseEmitter.send(SseEmitter.event().reconnectTime(5000));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SSE_POOL.put(key, sseEmitter);
        return sseEmitter;
    }

    public static void sendMessage(String key, String messageId, String message) {
        if (!SSE_POOL.containsKey(key)) {
            throw new RuntimeException(key + "不存在");
        }
        try {
            SSE_POOL.get(key).send(SseEmitter.event().id(messageId).reconnectTime(1*60*1000L).data(message));
        } catch (IOException e) {
            SSE_POOL.remove(key);
            e.printStackTrace();
        }
    }

    public static void closeSse(String key) {
        if (SSE_POOL.containsKey(key)) {
            SseEmitter sseEmitter = SSE_POOL.get(key);
            //数据发送完毕
            sseEmitter.complete();
            SSE_POOL.remove(key);
        }
    }
}
