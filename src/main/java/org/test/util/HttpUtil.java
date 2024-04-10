package org.test.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.TimeValue;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @description:
 * @Title: HttpUtil
 * @Author xlw
 * @Package com.xlw.test.util
 * @Date 2024/4/8 17:54
 */
public class HttpUtil {

    private HttpUtil() {
    }

    /**
     * HttpClient 对象
     */
    private static CloseableHttpClient httpClient = null;

    /**
     * CookieStore 对象
     */
    private static CookieStore cookieStore = null;

    /**
     * Basic Auth 管理对象
     **/
    private static BasicCredentialsProvider basicCredentialsProvider = null;

    /**
     * HttpClient初始化
     **/
    static {
        // 注册访问协议相关的 Socket 工厂
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        // Http 连接池
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
        poolingHttpClientConnectionManager.setDefaultSocketConfig(SocketConfig.custom()
                .setSoTimeout(15, TimeUnit.SECONDS)
                .setTcpNoDelay(true).build()
        );

        // 整个连接池最大连接数
        poolingHttpClientConnectionManager.setMaxTotal(1000);
        // 每路由最大连接数，默认值是2
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(200);
        poolingHttpClientConnectionManager.setValidateAfterInactivity(TimeValue.ofSeconds(15));
        // Http 请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000, TimeUnit.MILLISECONDS)
                .setResponseTimeout(5000, TimeUnit.MILLISECONDS)
                .setConnectionRequestTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
        // 设置 Cookie
        cookieStore = new BasicCookieStore();
        // 设置 Basic Auth 对象
        basicCredentialsProvider = new BasicCredentialsProvider();
        // 创建监听器，在 JVM 停止或重启时，关闭连接池释放掉连接
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        // 创建 HttpClient 对象
        httpClient = HttpClients.custom()
                // 设置 Cookie
                .setDefaultCookieStore(cookieStore)
                // 设置 Basic Auth
                .setDefaultCredentialsProvider(basicCredentialsProvider)
                // 设置 HttpClient 请求参数
                .setDefaultRequestConfig(requestConfig)
                // 设置连接池
                .setConnectionManager(poolingHttpClientConnectionManager)
                // 设置定时清理连接池中过期的连接
                .evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofSeconds(15))
                .setRetryStrategy(new DefaultHttpRequestRetryStrategy(2, TimeValue.ofSeconds(1L)))
                .build();
    }

    /**
     * 获取 Httpclient 对象
     */
    public static CloseableHttpClient getHttpclient() {
        return httpClient;
    }

    /**
     * 获取 CookieStore 对象
     */
    public static CookieStore getCookieStore() {
        return cookieStore;
    }

    /**
     * 获取 BasicCredentialsProvider 对象
     */
    public static BasicCredentialsProvider getBasicCredentialsProvider() {
        return basicCredentialsProvider;
    }

    /**
     * Http get 请求
     */
    public static String httpGet(String uri) {
        if (StrUtil.isBlank(uri)) {
            throw new NullPointerException("HttpClientUtil.uri.notEmpty!");
        }
        String result = "";
        CloseableHttpResponse response = null;
        try {
            // 创建 HttpGet 对象
            HttpGet httpGet = new HttpGet(uri);
            // 执行 Http Get 请求
            response = HttpUtil.getHttpclient().execute(httpGet);
            // 输出响应内容
            if (response.getEntity() != null) {
                result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
            // 销毁流
            EntityUtils.consume(response.getEntity());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * Http get 请求
     */
    public static String httpGet(String uri, Map<String, String> cookieMap, Map<String, String> headMap) {
        if (StrUtil.isBlank(uri)) {
            throw new NullPointerException("HttpClientUtil.uri.notEmpty!");
        }
        String result = "";
        CloseableHttpResponse response = null;
        try {
            // 创建 HttpGet 对象
            HttpGet httpGet = new HttpGet(uri);
            //增加cookie
            if (CollectionUtil.isNotEmpty(cookieMap)) {
                String cookie = cookieMap.entrySet().stream()
                        .map(n -> {
                            try {
                                return n.getKey() + "=" + URLEncoder.encode(n.getValue(), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }).collect(Collectors.joining(";"));
                if (StrUtil.isNotBlank(cookie)) {
                    httpGet.addHeader("Cookie", cookie);
                }
            }
            //增加head
            if (CollectionUtil.isNotEmpty(headMap)) {
                for (String key : headMap.keySet()) {
                    if (StrUtil.isNotBlank(key) && StrUtil.isNotBlank(headMap.get(key))) {
                        httpGet.addHeader(key, headMap.get(key));
                    }
                }
            }
            // 执行 Http Get 请求
            CloseableHttpClient httpclient = HttpUtil.getHttpclient();
            response = httpclient.execute(httpGet);
            // 输出响应内容
            if (response.getEntity() != null) {
                result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
            // 销毁流
            EntityUtils.consume(response.getEntity());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * Http Post Json 表单请求示例
     */
    @SneakyThrows
    public static void httpPostJson(String uri, Map<String, Object> reqArgs, Consumer<InputStream> reponseInputStream) {
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        try {
            if (StrUtil.isBlank(uri)) {
                throw new NullPointerException("HttpClientUtil.httpPostJson.uri.notEmpty!");
            }
            // 创建 HttpPost 对象
            HttpPost httpPost = new HttpPost(uri);

            StringEntity stringEntity = null;
            if (null != reqArgs && 0 != reqArgs.size()) {
                // 将请求对象通过 fastjson 中方法转换为 Json 字符串，并创建字符串实体对象
                stringEntity = new StringEntity(JSONUtil.toJsonStr(reqArgs), StandardCharsets.UTF_8);
                // 设置 HttpPost 请求参数
                httpPost.setEntity(stringEntity);
            }
            // 设置 Content-Type
            httpPost.addHeader("Content-Type", ContentType.APPLICATION_JSON);
            // 执行 Http Post 请求
            response = HttpUtil.getHttpclient().execute(httpPost);
            // 输出响应内容
            if (response.getEntity() != null) {
                inputStream = response.getEntity().getContent();
                if (null != inputStream) {
                    reponseInputStream.accept(inputStream);
                }
            }

            // 销毁流
            if (null != reqArgs && 0 != reqArgs.size() && null != stringEntity) {
                EntityUtils.consume(stringEntity);
            }
            EntityUtils.consume(response.getEntity());
        } catch (Exception e) {
            throw e;
        } finally {
            // 释放资源
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * post 请求
     */
    public static String executePost(String url, Map<String, Object> reqArgs, String authorization) {
        String result = null;
        if (null == reqArgs || 0 == reqArgs.size()) {
            return result;
        }
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(JSONUtil.toJsonStr(reqArgs), ContentType.create("application/json", "utf-8"));
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Content-Type", "application/json");
        if (StrUtil.isNotBlank(authorization)) {
            httpPost.setHeader("Authorization", authorization);
        }
        try {
            CloseableHttpResponse response = HttpUtil.getHttpclient().execute(httpPost);
            if (response.getCode() == HttpStatus.SC_OK || response.getCode() == HttpStatus.SC_CREATED) {
                result = getStreamAsString(response.getEntity().getContent(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset), 8192);
            StringWriter writer = new StringWriter();
            char[] chars = new char[8192];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }

            return writer.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public static void main(String[] args) {
        String s = HttpUtil.httpGet("http://www.baidu.com");
        System.out.println(s);
        System.out.println("hello");
    }
}
