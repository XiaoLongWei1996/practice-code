package com.springcloud.test.alibabaconsumer.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;

import java.util.Map;

/**
 * 令牌跑龙套
 *
 * @author 肖龙威
 * @date 2022/11/25 16:39
 */
public class TokenUtil {

    private static JWTSigner singer = JWTSignerUtil.hs512("abcd.".getBytes());

    /**
     * 创建令牌
     *
     * @param payload 有效载荷
     * @return {@link String}
     */
    public static String createToken(Map<String, Object> payload) {
        DateTime now = DateTime.now();
        DateTime expire = now.offsetNew(DateField.MINUTE, 15);
        String token = JWT.create()
                .addPayloads(payload)
//                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiresAt(expire)
                .setSigner(singer)
                .sign();
        return token;
    }

    /**
     * 验证令牌
     *
     * @param token 令牌
     * @return boolean
     */
    public static boolean validateToken(String token) {
        return JWT.of(token).setSigner(singer).validate(0);
    }

    /**
     * 解析令牌
     *
     * @param token 令牌
     * @param clazz clazz
     * @return {@link T}
     */
    public static <T> T parseToken(String token, Class<T> clazz) {
        JSONObject json = JWT.of(token).getPayloads();
        return json.toBean(clazz);
    }
}
