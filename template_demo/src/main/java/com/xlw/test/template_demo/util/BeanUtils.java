package com.xlw.test.template_demo.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Supplier;

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
        JSONConfig config = new JSONConfig();
        config.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        config.setIgnoreNullValue(false);
        JSONObject jsonObject = new JSONObject(config);
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

    /**
     * Optional获取对象不为空返回source,为空返回back
     *
     * @param source 源
     * @param back   空值返回值
     * @return {@link R }
     */
    public static <R> R optionalGet(R source, R back) {
        return Optional.ofNullable(source).orElse(back);
    }

    /**
     * Optional获取对象不为空返回source,为空执行back
     *
     * @param source 源
     * @param back   返回
     * @return {@link R }
     */
    public static <R> R optionalGet(R source, Supplier<R> back) {
        return Optional.ofNullable(source).orElseGet(back);
    }

    /**
     * 子字符串
     *
     * @param str   str
     * @param start 开始
     * @param end   结束
     * @return {@link String }
     */
    public static String subString(String str, int start, int end) {
        if (StrUtil.isBlank(str)) {
            return str;
        }
        int length = str.length();
        int length1 = end - start;
        if (length <= length1) {
            return str;
        }
        return str.substring(start, end);
    }

    /**
     * @param params 参数
     * @return {@link String }
     */
    public static String beanToUrlParams(Object params) {
        JSONConfig config = new JSONConfig();
        config.setIgnoreNullValue(true);
        config.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        JSONObject obj = JSONUtil.parseObj(params, config);
        StringJoiner sj = new StringJoiner("&");
        obj.forEach((k, v) -> {
            sj.add(k + "=" + v);
        });
        return sj.toString();
    }

    public static void main(String[] args) {
        String localMacAddress = NetUtil.getLocalMacAddress();
        String code = localMacAddress + IdUtil.fastUUID();
        System.out.println(MD5.create().digestHex(code).hashCode());
        System.out.println(localMacAddress);
    }
}
