package com.xlw.test.template_demo.controller;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.json.JSONObject;
import com.sun.net.httpserver.Headers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @description:
 * @Title: NotifyController
 * @Author xlw
 * @Package com.xlw.test.template_demo.controller
 * @Date 2024/10/23 10:21
 */
@RestController
@RequestMapping("notify")
public class NotifyController {

    @PostMapping("kpjg")
    public String kpjg(@RequestBody String msg, HttpServletRequest request) {
        System.out.println(msg);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ":" + value);
        }
        return "SUCCESS";
    }


    @PostMapping("jyjg")
    public String jyjg(@RequestBody String msg, HttpServletRequest request) {
        System.out.println(msg);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ":" + value);
        }
        return "SUCCESS";
    }
}
