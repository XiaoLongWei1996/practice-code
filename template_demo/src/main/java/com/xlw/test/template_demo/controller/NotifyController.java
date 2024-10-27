package com.xlw.test.template_demo.controller;

import cn.hutool.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String kpjg(@RequestBody String msg) {
        System.out.println(msg);
        JSONObject object = new JSONObject();
        object.put("code", "0");
        object.put("success", true);
        return object.toString();
    }


    @PostMapping("jyjg")
    public String jyjg(@RequestBody String msg) {
        System.out.println(msg);
        return "SUCCESS";
    }
}
