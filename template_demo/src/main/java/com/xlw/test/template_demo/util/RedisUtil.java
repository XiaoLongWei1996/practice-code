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
    private RedisTemplate<String, Object> redisTemplate;

    /*-------------------------------------------------------key操作-------------------------------------------------------------*/

    /**
     * 有关键
     *
     * @param key 关键
     * @return boolean
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除
     *
     * @param key 关键
     */
    public void delete(String... key) {
        redisTemplate.delete(Arrays.asList(key));
    }

    /**
     * ttl
     *
     * @param key 关键
     * @return long
     */
    public long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * @param key      关键
     * @param timeout
     * @param timeUnit
     */
    public void expire(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * @param pattern
     * @return {@link Set}<{@link String}>
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * @param key 关键
     */
    public void unlink(String key) {
        redisTemplate.unlink(key);
    }

    /*---------------------------------------------------String操作--------------------------------------------------------------*/

    /**
     * @param key   关键
     * @param value
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * @param key      关键
     * @param value
     * @param timeout
     * @param timeUnit
     */
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * @param key      关键
     * @param value
     * @param timeout
     * @param timeUnit
     */
    public void setNx(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }

    /**
     * @param key 关键
     * @return {@link Object}
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * @param key   关键
     * @param value
     * @return {@link Object}
     */
    public Object getSet(String key, Object value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * @param key 关键
     * @return long
     */
    public long length(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    /**
     * @param key 关键
     * @return long
     */
    public long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * @param key   关键
     * @param delta
     * @return long
     */
    public long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * @param key 关键
     * @return long
     */
    public long decr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * @param key   关键
     * @param delta
     * @return long
     */
    public long decr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /*-------------------------------------------------list操作----------------------------------------------------------------*/

    /**
     * @param key   关键
     * @param value
     */
    public void leftPush(String key, Object... value) {
        redisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * @param key   关键
     * @param value
     */
    public void rightPush(String key, Object... value) {
        redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * @param key   关键
     * @param count
     * @param value 价值
     */
    public void lRemove(String key, long count, Object value) {
        redisTemplate.opsForList().remove(key, count, value);
    }

    /**
     * l组
     *
     * @param key   关键
     * @param index 指数
     * @param value 价值
     */
    public void lSet(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * l len
     *
     * @param key 关键
     * @return long
     */
    public long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * l指数
     *
     * @param key   关键
     * @param index 指数
     * @return {@link Object}
     */
    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * @param key   关键
     * @param start
     * @param end
     * @return {@link List}<{@link Object}>
     */
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * @param key   关键
     * @param start
     * @param end
     */
    public void lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * @param key 关键
     * @return {@link Object}
     */
    public Object lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * @param key 关键
     * @return {@link Object}
     */
    public Object rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /*------------------------------------------------set操作--------------------------------------------------------------*/

    /**
     * @param key   关键
     * @param value 价值
     */
    public void sSet(String key, Object... value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * @param key   关键
     * @param value 价值
     */
    public void sRemove(String key, Object... value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    /**
     * @param key 关键
     * @return long
     */
    public long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * @param key   关键
     * @param value 价值
     * @return boolean
     */
    public boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * @param key 关键
     * @return {@link Object}
     */
    public Object sPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * @param key 关键
     * @return {@link Set}<{@link Object}>
     */
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * @param key     关键
     * @param pattern
     * @param count
     * @return {@link Cursor}<{@link Object}>
     */
    public Cursor<Object> sScan(String key, String pattern, long count) {
        ScanOptions so = ScanOptions.scanOptions().match(pattern).count(count).build();
        return redisTemplate.opsForSet().scan(key, so);
    }

    /**
     * @param key   关键
     * @param value 价值
     * @param score
     *//*----------------------------------------------------zset操作-----------------------------------------------------------------*/
    public void zAdd(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * @param key   关键
     * @param value 价值
     */
    public void zAddAll(String key, ZSetOperations.TypedTuple<Object>... value) {
        Set<ZSetOperations.TypedTuple<Object>> set = new HashSet<>();
        set.addAll(Arrays.asList(value));
        redisTemplate.opsForZSet().add(key, set);
    }

    /**
     * @param key   关键
     * @param value 价值
     */
    public void zRemove(String key, Object... value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    /**
     * @param key   关键
     * @param start
     * @param end
     */
    public void zRemoveRange(String key, long start, long end) {
        redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * @param key   关键
     * @param start
     * @param end
     */
    public void zRemoveRangeByScore(String key, double start, double end) {
        redisTemplate.opsForZSet().removeRangeByScore(key, start, end);
    }

    /**
     * @param key   关键
     * @param value 价值
     * @param delta
     */
    public void zIncr(String key, Object value, double delta) {
        redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * @param key   关键
     * @param start
     * @param end
     * @return {@link Set}<{@link Object}>
     */
    public Set<Object> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * @param key   关键
     * @param start
     * @param end
     * @return {@link Set}<{@link Object}>
     */
    public Set<Object> zRevRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * @param key   关键
     * @param start 开始
     * @param end   结束
     * @return {@link Set}<{@link Object}>
     */
    public Set<Object> zRangeByScore(String key, double start, double end) {
        return redisTemplate.opsForZSet().rangeByScore(key, start, end);
    }

    /**
     * z排名
     *
     * @param key   关键
     * @param value 价值
     * @return long
     */
    public long zRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * Z转阶
     *
     * @param key   关键
     * @param value 价值
     * @return long
     */
    public long zRevRank(String key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * z大小
     *
     * @param key 关键
     * @return long
     */
    public long zSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * @param key   关键
     * @param value 价值
     * @return double
     */
    public double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * @param key 关键
     * @param min
     * @param max
     * @return long
     */
    public long zCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /*-------------------------------------------------hash操作------------------------------------------------------------*/

    /**
     * @param key     关键
     * @param hashKey
     * @param value   价值
     */
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * @param key 关键
     * @param map
     */
    public void hSetAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * @param key     关键
     * @param hashKey
     * @param value   价值
     */
    public void hSetNx(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    /**
     * @param key     关键
     * @param hashKey
     */
    public void hDelete(String key, String... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * @param key     关键
     * @param hashKey
     * @param delta
     */
    public void hIncr(String key, String hashKey, long delta) {
        redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * @param key     关键
     * @param hashKey
     * @param delta
     */
    public void hIncrByFloat(String key, String hashKey, double delta) {
        redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * @param key     关键
     * @param hashKey
     * @return {@link Object}
     */
    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * @param key 关键
     * @return {@link Map}<{@link Object}, {@link Object}>
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * @param key     关键
     * @param hashKey
     * @return boolean
     */
    public boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * @param key 关键
     * @return long
     */
    public long hSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * @param key      关键
     * @param hashKeys
     * @return {@link List}<{@link Object}>
     */
    public List<Object> hMultiGet(String key, Collection<Object> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    /**
     * @param key     关键
     * @param hashKey
     * @return boolean
     */
    public boolean hExists(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * @param key 关键
     * @return {@link Set}<{@link Object}>
     */
    public Set<Object> hKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * @param key 关键
     * @return {@link List}<{@link Object}>
     */
    public List<Object> hValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /*--------------------------------------------------------HyperLogLog操作--------------------------------------------------------*/

    /**
     * @param key   关键
     * @param value 价值
     */
    public void pfAdd(String key, Object... value) {
        redisTemplate.opsForHyperLogLog().add(key, value);
    }

    /**
     * @param key 关键
     * @return long
     */
    public long pfCount(String key) {
        return redisTemplate.opsForHyperLogLog().size(key);
    }

    /**
     * @param resultClass
     * @param luaPath
     * @param keys        键
     * @param args        arg游戏
     * @return {@link R}
     *//*----------------------------------------------------执行lua脚本-----------------------------------------------------------------*/
    public <R> R execute(Class<R> resultClass, String luaPath, List<String> keys, Object... args) {
        DefaultRedisScript<R> script = new DefaultRedisScript();
        script.setLocation(new ClassPathResource(luaPath));
        script.setResultType(resultClass);
        return redisTemplate.execute(script, keys, args);
    }
}
