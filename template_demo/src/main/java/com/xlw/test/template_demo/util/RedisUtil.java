package com.xlw.test.template_demo.util;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 复述,实效
 *
 * @description: Redis工具类
 * @Title: RedisUtil
 * @Author xlw
 * @Package com.security.common.util
 * @Date 2024/3/26 11:12
 */
@AllArgsConstructor
public class RedisUtil {

    /**
     * redis模板
     */
    private static RedisTemplate<String, Object> redisTemplate;

    /*-------------------------------------------------------key操作-------------------------------------------------------------*/

    /**
     * 获取 Redis 模板
     *
     * @return {@link RedisTemplate }<{@link String }, {@link Object }>
     */
    private static RedisTemplate<String, Object> getRedisTemplate() {
        if (Objects.isNull(redisTemplate)) {
            redisTemplate = (RedisTemplate<String, Object>) ApplicationContextUtil.getBean("redisTemplate");
        }
        return redisTemplate;
    }

    /**
     * 有关键
     *
     * @param key 关键
     * @return boolean
     */
    public static boolean hasKey(String key) {
        return getRedisTemplate().hasKey(key);
    }

    /**
     * 删除
     *
     * @param key 关键
     */
    public static void delete(String... key) {
        getRedisTemplate().delete(Arrays.asList(key));
    }

    /**
     * ttl
     *
     * @param key 关键
     * @return long
     */
    public static long ttl(String key) {
        return getRedisTemplate().getExpire(key);
    }

    /**
     * @param key      关键
     * @param timeout
     * @param timeUnit
     */
    public static void expire(String key, long timeout, TimeUnit timeUnit) {
        getRedisTemplate().expire(key, timeout, timeUnit);
    }

    /**
     * @param pattern
     * @return {@link Set}<{@link String}>
     */
    public static Set<String> keys(String pattern) {
        return getRedisTemplate().keys(pattern);
    }

    /**
     * @param key 关键
     */
    public static void unlink(String key) {
        getRedisTemplate().unlink(key);
    }

    /*---------------------------------------------------String操作--------------------------------------------------------------*/

    /**
     * @param key   关键
     * @param value
     */
    public static void set(String key, Object value) {
        getRedisTemplate().opsForValue().set(key, value);
    }

    /**
     * @param key      关键
     * @param value
     * @param timeout
     * @param timeUnit
     */
    public static void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        getRedisTemplate().opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * @param key      关键
     * @param value
     * @param timeout
     * @param timeUnit
     */
    public static void setNx(String key, Object value, long timeout, TimeUnit timeUnit) {
        getRedisTemplate().opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }

    /**
     * @param key 关键
     * @return {@link Object}
     */
    public static Object get(String key) {
        return getRedisTemplate().opsForValue().get(key);
    }

    /**
     * @param key   关键
     * @param value
     * @return {@link Object}
     */
    public static Object getSet(String key, Object value) {
        return getRedisTemplate().opsForValue().getAndSet(key, value);
    }

    /**
     * @param key 关键
     * @return long
     */
    public static long length(String key) {
        return getRedisTemplate().opsForValue().size(key);
    }

    /**
     * @param key 关键
     * @return long
     */
    public static long incr(String key) {
        return getRedisTemplate().opsForValue().increment(key);
    }

    /**
     * @param key   关键
     * @param delta
     * @return long
     */
    public static long incr(String key, long delta) {
        return getRedisTemplate().opsForValue().increment(key, delta);
    }

    /**
     * @param key 关键
     * @return long
     */
    public static long decr(String key) {
        return getRedisTemplate().opsForValue().increment(key);
    }

    /**
     * @param key   关键
     * @param delta
     * @return long
     */
    public static long decr(String key, long delta) {
        return getRedisTemplate().opsForValue().increment(key, delta);
    }

    /*-------------------------------------------------list操作----------------------------------------------------------------*/

    /**
     * @param key   关键
     * @param value
     */
    public static void leftPush(String key, Object... value) {
        getRedisTemplate().opsForList().leftPushAll(key, value);
    }

    /**
     * @param key   关键
     * @param value
     */
    public static void rightPush(String key, Object... value) {
        getRedisTemplate().opsForList().rightPushAll(key, value);
    }

    /**
     * @param key   关键
     * @param count
     * @param value 价值
     */
    public static void lRemove(String key, long count, Object value) {
        getRedisTemplate().opsForList().remove(key, count, value);
    }

    /**
     * l组
     *
     * @param key   关键
     * @param index 指数
     * @param value 价值
     */
    public static void lSet(String key, long index, Object value) {
        getRedisTemplate().opsForList().set(key, index, value);
    }

    /**
     * l len
     *
     * @param key 关键
     * @return long
     */
    public static long lLen(String key) {
        return getRedisTemplate().opsForList().size(key);
    }

    /**
     * l指数
     *
     * @param key   关键
     * @param index 指数
     * @return {@link Object}
     */
    public static Object lIndex(String key, long index) {
        return getRedisTemplate().opsForList().index(key, index);
    }

    /**
     * @param key   关键
     * @param start
     * @param end
     * @return {@link List}<{@link Object}>
     */
    public static List<Object> lRange(String key, long start, long end) {
        return getRedisTemplate().opsForList().range(key, start, end);
    }

    /**
     * @param key   关键
     * @param start
     * @param end
     */
    public static void lTrim(String key, long start, long end) {
        getRedisTemplate().opsForList().trim(key, start, end);
    }

    /**
     * @param key 关键
     * @return {@link Object}
     */
    public static Object lPop(String key) {
        return getRedisTemplate().opsForList().leftPop(key);
    }

    /**
     * @param key 关键
     * @return {@link Object}
     */
    public static Object rPop(String key) {
        return getRedisTemplate().opsForList().rightPop(key);
    }

    /*------------------------------------------------set操作--------------------------------------------------------------*/

    /**
     * @param key   关键
     * @param value 价值
     */
    public static void sSet(String key, Object... value) {
        getRedisTemplate().opsForSet().add(key, value);
    }

    /**
     * @param key   关键
     * @param value 价值
     */
    public static void sRemove(String key, Object... value) {
        getRedisTemplate().opsForSet().remove(key, value);
    }

    /**
     * @param key 关键
     * @return long
     */
    public static long sSize(String key) {
        return getRedisTemplate().opsForSet().size(key);
    }

    /**
     * @param key   关键
     * @param value 价值
     * @return boolean
     */
    public static boolean sIsMember(String key, Object value) {
        return getRedisTemplate().opsForSet().isMember(key, value);
    }

    /**
     * @param key 关键
     * @return {@link Object}
     */
    public static Object sPop(String key) {
        return getRedisTemplate().opsForSet().pop(key);
    }

    /**
     * @param key 关键
     * @return {@link Set}<{@link Object}>
     */
    public static Set<Object> sMembers(String key) {
        return getRedisTemplate().opsForSet().members(key);
    }

    /**
     * @param key     关键
     * @param pattern
     * @param count
     * @return {@link Cursor}<{@link Object}>
     */
    public static Cursor<Object> sScan(String key, String pattern, long count) {
        ScanOptions so = ScanOptions.scanOptions().match(pattern).count(count).build();
        return getRedisTemplate().opsForSet().scan(key, so);
    }

    /**
     * @param key   关键
     * @param value 价值
     * @param score
     *//*----------------------------------------------------zset操作-----------------------------------------------------------------*/
    public static void zAdd(String key, Object value, double score) {
        getRedisTemplate().opsForZSet().add(key, value, score);
    }

    /**
     * @param key   关键
     * @param value 价值
     */
    public static void zAddAll(String key, ZSetOperations.TypedTuple<Object>... value) {
        Set<ZSetOperations.TypedTuple<Object>> set = new HashSet<>();
        set.addAll(Arrays.asList(value));
        getRedisTemplate().opsForZSet().add(key, set);
    }

    /**
     * @param key   关键
     * @param value 价值
     */
    public static void zRemove(String key, Object... value) {
        getRedisTemplate().opsForZSet().remove(key, value);
    }

    /**
     * @param key   关键
     * @param start
     * @param end
     */
    public static void zRemoveRange(String key, long start, long end) {
        getRedisTemplate().opsForZSet().removeRange(key, start, end);
    }

    /**
     * @param key   关键
     * @param start
     * @param end
     */
    public static void zRemoveRangeByScore(String key, double start, double end) {
        getRedisTemplate().opsForZSet().removeRangeByScore(key, start, end);
    }

    /**
     * @param key   关键
     * @param value 价值
     * @param delta
     */
    public static void zIncr(String key, Object value, double delta) {
        getRedisTemplate().opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * @param key   关键
     * @param start
     * @param end
     * @return {@link Set}<{@link Object}>
     */
    public static Set<Object> zRange(String key, long start, long end) {
        return getRedisTemplate().opsForZSet().range(key, start, end);
    }

    /**
     * @param key   关键
     * @param start
     * @param end
     * @return {@link Set}<{@link Object}>
     */
    public static Set<Object> zRevRange(String key, long start, long end) {
        return getRedisTemplate().opsForZSet().reverseRange(key, start, end);
    }

    /**
     * @param key   关键
     * @param start 开始
     * @param end   结束
     * @return {@link Set}<{@link Object}>
     */
    public static Set<Object> zRangeByScore(String key, double start, double end) {
        return getRedisTemplate().opsForZSet().rangeByScore(key, start, end);
    }

    /**
     * z排名
     *
     * @param key   关键
     * @param value 价值
     * @return long
     */
    public static long zRank(String key, Object value) {
        return getRedisTemplate().opsForZSet().rank(key, value);
    }

    /**
     * Z转阶
     *
     * @param key   关键
     * @param value 价值
     * @return long
     */
    public static long zRevRank(String key, Object value) {
        return getRedisTemplate().opsForZSet().reverseRank(key, value);
    }

    /**
     * z大小
     *
     * @param key 关键
     * @return long
     */
    public static long zSize(String key) {
        return getRedisTemplate().opsForZSet().size(key);
    }

    /**
     * @param key   关键
     * @param value 价值
     * @return double
     */
    public static double zScore(String key, Object value) {
        return getRedisTemplate().opsForZSet().score(key, value);
    }

    /**
     * @param key 关键
     * @param min
     * @param max
     * @return long
     */
    public static long zCount(String key, double min, double max) {
        return getRedisTemplate().opsForZSet().count(key, min, max);
    }

    /*-------------------------------------------------hash操作------------------------------------------------------------*/

    /**
     * @param key     关键
     * @param hashKey
     * @param value   价值
     */
    public static void hSet(String key, String hashKey, Object value) {
        getRedisTemplate().opsForHash().put(key, hashKey, value);
    }

    /**
     * @param key 关键
     * @param map
     */
    public static void hSetAll(String key, Map<String, Object> map) {
        getRedisTemplate().opsForHash().putAll(key, map);
    }

    /**
     * @param key     关键
     * @param hashKey
     * @param value   价值
     */
    public static void hSetNx(String key, String hashKey, Object value) {
        getRedisTemplate().opsForHash().putIfAbsent(key, hashKey, value);
    }

    /**
     * @param key     关键
     * @param hashKey
     */
    public static void hDelete(String key, String... hashKey) {
        getRedisTemplate().opsForHash().delete(key, hashKey);
    }

    /**
     * @param key     关键
     * @param hashKey
     * @param delta
     */
    public static void hIncr(String key, String hashKey, long delta) {
        getRedisTemplate().opsForHash().increment(key, hashKey, delta);
    }

    /**
     * @param key     关键
     * @param hashKey
     * @param delta
     */
    public static void hIncrByFloat(String key, String hashKey, double delta) {
        getRedisTemplate().opsForHash().increment(key, hashKey, delta);
    }

    /**
     * @param key     关键
     * @param hashKey
     * @return {@link Object}
     */
    public static Object hGet(String key, String hashKey) {
        return getRedisTemplate().opsForHash().get(key, hashKey);
    }

    /**
     * @param key 关键
     * @return {@link Map}<{@link Object}, {@link Object}>
     */
    public static Map<Object, Object> hGetAll(String key) {
        return getRedisTemplate().opsForHash().entries(key);
    }

    /**
     * @param key     关键
     * @param hashKey
     * @return boolean
     */
    public static boolean hHasKey(String key, String hashKey) {
        return getRedisTemplate().opsForHash().hasKey(key, hashKey);
    }

    /**
     * @param key 关键
     * @return long
     */
    public static long hSize(String key) {
        return getRedisTemplate().opsForHash().size(key);
    }

    /**
     * @param key      关键
     * @param hashKeys
     * @return {@link List}<{@link Object}>
     */
    public static List<Object> hMultiGet(String key, Collection<Object> hashKeys) {
        return getRedisTemplate().opsForHash().multiGet(key, hashKeys);
    }

    /**
     * @param key     关键
     * @param hashKey
     * @return boolean
     */
    public static boolean hExists(String key, String hashKey) {
        return getRedisTemplate().opsForHash().hasKey(key, hashKey);
    }

    /**
     * @param key 关键
     * @return {@link Set}<{@link Object}>
     */
    public static Set<Object> hKeys(String key) {
        return getRedisTemplate().opsForHash().keys(key);
    }

    /**
     * @param key 关键
     * @return {@link List}<{@link Object}>
     */
    public static List<Object> hValues(String key) {
        return getRedisTemplate().opsForHash().values(key);
    }

    /*--------------------------------------------------------HyperLogLog操作--------------------------------------------------------*/

    /**
     * @param key   关键
     * @param value 价值
     */
    public static void pfAdd(String key, Object... value) {
        getRedisTemplate().opsForHyperLogLog().add(key, value);
    }

    /**
     * @param key 关键
     * @return long
     */
    public static long pfCount(String key) {
        return getRedisTemplate().opsForHyperLogLog().size(key);
    }

    /**
     * @param resultClass 返回类型
     * @param luaPath     lua文件路径
     * @param keys        键
     * @param args        arg游戏
     * @return {@link R}
     *//*----------------------------------------------------执行lua脚本-----------------------------------------------------------------*/
    public static <R> R executeLuaFile(Class<R> resultClass, String luaPath, List<String> keys, Object... args) {
        DefaultRedisScript<R> script = new DefaultRedisScript();
        script.setLocation(new ClassPathResource(luaPath));
        script.setResultType(resultClass);
        return getRedisTemplate().execute(script, keys, args);
    }

    /**
     * 执行 Lua
     *
     * @param resultClass 返回类型
     * @param luaScript   Lua 脚本
     * @param keys        钥匙
     * @param args        参数
     * @return {@link R }
     */
    public static <R> R executeLua(Class<R> resultClass, String luaScript, List<String> keys, Object... args) {
        DefaultRedisScript<R> script = new DefaultRedisScript();
        script.setScriptText(luaScript);
        script.setResultType(resultClass);
        return getRedisTemplate().execute(script, keys, args);
    }
}
