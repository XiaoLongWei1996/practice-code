package org.test.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.AlgorithmUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;

import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwtutil
 *
 * @description: jwt工具类
 * @Title: JWTUtil
 * @Author xlw
 * @Package org.test.util
 * @Date 2023/12/14 16:27
 */
public class JWTUtil {


    /**
     * 对称
     */
    private static final String SYMMETRIC = "hs256";

    /**
     * 不对称
     */
    private static final String ASYMMETRIC = "rs256";

    /**
     * 创建对称JWT
     *
     * @param payloads  有效载荷
     * @param expiresAt 到期
     * @param key       关键
     * @return {@link String}
     */
    public static String createSymmetricJWT(Map<String, Object> payloads, Date expiresAt, byte[] key) {
        return JWT.create()
                .addPayloads(payloads)
                .setPayload("user", new Ss("小明", 15))
                .setExpiresAt(expiresAt)
                .setIssuedAt(new Date())
                .setCharset(Charset.forName("UTF-8"))
                .sign(JWTSignerUtil.createSigner(SYMMETRIC, key));
    }

    /**
     * 创建非对称JWT
     *
     * @param payloads   有效载荷
     * @param expiresAt  到期
     * @param privateKey 私钥
     * @return {@link String}
     */
    public static String createAsymmetricJWT(Map<String, Object> payloads, Date expiresAt, byte[] privateKey) {
        return JWT.create()
                .addPayloads(payloads)
                .setPayload("user", new Ss("小明", 15))
                .setExpiresAt(expiresAt)
                .setIssuedAt(new Date())
                .setCharset(Charset.forName("UTF-8"))
                .sign(JWTSignerUtil.createSigner(ASYMMETRIC, KeyUtil.generatePrivateKey(AlgorithmUtil.getAlgorithm(ASYMMETRIC), privateKey)));
    }

    /**
     * 对称jwtvalid吗
     *
     * @param token 令牌
     * @param key   关键
     * @return boolean
     */
    public static boolean isSymmetricJWTValid(String token, byte[] key) {
        return JWT.of(token).setCharset(Charset.forName("UTF-8")).setSigner(SYMMETRIC, key).validate(0);
    }

    /**
     * 不对称是否有效
     *
     * @param token     令牌
     * @param publicKey 公钥
     * @return boolean
     */
    public static boolean isAsymmetricJWTValid(String token, byte[] publicKey) {
        return JWT.of(token).setCharset(Charset.forName("UTF-8")).setSigner(ASYMMETRIC, KeyUtil.generatePublicKey(AlgorithmUtil.getAlgorithm(ASYMMETRIC), publicKey)).validate(0);
    }

    /**
     * 验证对称JWT
     *
     * @param token 令牌
     * @param key   关键
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    public static Map<String, Object> verifySymmetricJWT(String token, byte[] key) {
        if (!isSymmetricJWTValid(token, key)) {
            throw new RuntimeException("token 验证失败");
        }
        return JWT.of(token)
                .setSigner(SYMMETRIC, key)
                .setCharset(Charset.forName("UTF-8"))
                .getPayloads();
    }

    /**
     * 验证非对称JWT
     *
     * @param token     令牌
     * @param publicKey 公钥
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    public static Map<String, Object> verifyAsymmetricJWT(String token, byte[] publicKey) {
        if (!isAsymmetricJWTValid(token, publicKey)) {
            throw new RuntimeException("token 验证失败");
        }
        return JWT.of(token)
                .setSigner(ASYMMETRIC, KeyUtil.generatePublicKey(AlgorithmUtil.getAlgorithm(ASYMMETRIC), publicKey))
                .setCharset(Charset.forName("UTF-8"))
                .getPayloads();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        KeyPair rs256 = KeyUtil.generateKeyPair(AlgorithmUtil.getAlgorithm(ASYMMETRIC));
        PrivateKey privateKey = rs256.getPrivate();
        PublicKey publicKey = rs256.getPublic();
        String jwt = JWTUtil.createAsymmetricJWT(new HashMap<>(), DateUtil.offsetMinute(new Date(), -30), privateKey.getEncoded());
        Map<String, Object> stringObjectMap = JWTUtil.verifyAsymmetricJWT(jwt, publicKey.getEncoded());

//        Map<String, Object> stringObjectMap = JWTUtil.verifyAsymmetricJWT(jwt, publicKey.getEncoded());
//        String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjp7Im5hbWUiOiLlsI_mmI4iLCJhZ2UiOjE1fSwiZXhwIjoxNzAyNzM3MTA0LCJpYXQiOjE3MDI3MzUzMDR9.vEisR5155cey-QqR-ThIhQL-kRjxDhQ-geHIMjzh34E";
        System.out.println(jwt);


    }

}
