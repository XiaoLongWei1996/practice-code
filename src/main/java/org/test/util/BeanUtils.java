package org.test.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @description: bean工具类
 * @Title: BeanUtils
 * @Author xlw
 * @Package com.invoice.tcc.util
 * @Date 2024/9/4 10:52
 */
public class BeanUtils {

    /**
     * Url参数转bean
     *
     * @param params ?name=a&age=12 格式参数
     * @param clazz  clazz
     * @return {@link R }
     */
    public static <R> R urlParamsToBean(String params, Class<R> clazz) throws UnsupportedEncodingException {
        Assert.notBlank(params, "参数不能为空");
        Assert.notNull(clazz, "class不能为空");
        String urlParams = params;
        if (params.startsWith("?")) {
            urlParams = params.substring(1);
        }
        String[] paramArr = urlParams.split("&");
        JSONObject jsonObject = new JSONObject();
        for (String param : paramArr) {
            String[] arr = param.split("=");
            String key = arr[0];
            if (arr.length == 2) {
                String value = arr[1];
                jsonObject.set(key, URLDecoder.decode(value, "UTF-8"));
            } else {
                jsonObject.set(key, null);
            }
        }
        return jsonObject.toBean(clazz);
    }
}
