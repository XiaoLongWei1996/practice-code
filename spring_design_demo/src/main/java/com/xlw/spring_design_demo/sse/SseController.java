package com.xlw.spring_design_demo.sse;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @module:
 * @Title: SseController
 * @Author xlw
 * @Package com.xlw.spring_design_demo.sse
 * @Date 2025/1/18 16:47
 */
@Controller
@RequestMapping("sse")
public class SseController {

    @GetMapping(path = "createSse", produces = "text/event-stream")
    public SseEmitter createSse(String key, HttpServletResponse respons) {
        respons.setContentType("text/event-stream");
        return SseClient.createSse(key);
    }

    @ResponseBody
    @GetMapping("sendMessage")
    public Boolean sendMessage(String key, String messageId, String message) {
        SseClient.sendMessage(key, messageId, message);
        return true;
    }

    @ResponseBody
    @GetMapping("closeSse")
    public Boolean closeSse(String key) {
        SseClient.closeSse(key);
        return true;
    }

    @GetMapping(path = "createEmitter")
    public ResponseEntity<ResponseBodyEmitter> createEmitter() {
        // 创建一个ResponseBodyEmitter，-1代表不超时
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        emitter.onCompletion(() -> {
            System.out.println("完成数据发送");
        });
        emitter.onTimeout(() -> {
            System.out.println("超时");
        });
        emitter.onError(e -> {
            System.out.println("发生异常");
        });
        new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    emitter.send("Progress: " + i + "%\r\n", MediaType.TEXT_HTML);
                    Thread.sleep(100);
                    System.out.println("发送：" + i);
                }
                emitter.complete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        }).start();
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(emitter);
    }

    @GetMapping(path = "createStreamingResponseBody")
    public ResponseEntity<StreamingResponseBody> createStreamingResponseBody() {
        StreamingResponseBody responseBody = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                for (int i = 0; i < 100; i++) {
                    outputStream.write(("Progress: " + i + "%\r\n").getBytes());
                    try {
                        Thread.sleep(100);
                        outputStream.flush();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("发送：" + i);
                }
                outputStream.close();
            }
        };
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(responseBody);
    }
}
