package com.xlw.test.redis_cache_test.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description: redis操作工具类
 * @Title: RedisUtil
 * @Author xlw
 * @Package com.xlw.test.redis_cache_test.util
 * @Date 2023/12/23 19:38
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /*-------------------------------------------------------key操作-------------------------------------------------------------*/

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void delete(String... key) {
        redisTemplate.delete(Arrays.asList(key));
    }

    public long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    public void expire(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public void unlink(String key) {
        redisTemplate.unlink(key);
    }

    /*---------------------------------------------------String操作--------------------------------------------------------------*/

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public void setNx(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Object getSet(String key, Object value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    public long length(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    public long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public long decr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public long decr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /*-------------------------------------------------list操作----------------------------------------------------------------*/

    public void leftPush(String key, Object... value) {
        redisTemplate.opsForList().leftPushAll(key, value);
    }

    public void rightPush(String key, Object... value) {
        redisTemplate.opsForList().rightPushAll(key, value);
    }

    public void lRemove(String key, long count, Object value) {
        redisTemplate.opsForList().remove(key, count, value);
    }

    public void lSet(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    public long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public void lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    public Object lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public Object rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /*------------------------------------------------set操作--------------------------------------------------------------*/

    public void sSet(String key, Object... value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public void sRemove(String key, Object... value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    public long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public Object sPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public Cursor<Object> sScan(String key, String pattern, long count) {
        ScanOptions so = ScanOptions.scanOptions().match(pattern).count(count).build();
        return redisTemplate.opsForSet().scan(key, so);
    }

    /*----------------------------------------------------zset操作-----------------------------------------------------------------*/
    public void zAdd(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    public void zAddAll(String key, ZSetOperations.TypedTuple<Object>... value) {
        Set<ZSetOperations.TypedTuple<Object>> set = new HashSet<>();
        set.addAll(Arrays.asList(value));
        redisTemplate.opsForZSet().add(key, set);
    }

    public void zRemove(String key, Object... value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    public void zRemoveRange(String key, long start, long end) {
        redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    public void zRemoveRangeByScore(String key, double start, double end) {
        redisTemplate.opsForZSet().removeRangeByScore(key, start, end);
    }

    public void zIncr(String key, Object value, double delta) {
        redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    public Set<Object> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    public Set<Object> zRevRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    public Set<Object> zRangeByScore(String key, double start, double end) {
        return redisTemplate.opsForZSet().rangeByScore(key, start, end);
    }

    public long zRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    public long zRevRank(String key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    public long zSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    public double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    public long zCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /*-------------------------------------------------hash操作------------------------------------------------------------*/

    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public void hSetAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public void hSetNx(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    public void hDelete(String key, String... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    public void hIncr(String key, String hashKey, long delta) {
        redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    public void hIncrByFloat(String key, String hashKey, double delta) {
        redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public long hSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    public List<Object> hMultiGet(String key, Collection<Object> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    public boolean hExists(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public Set<Object> hKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    public List<Object> hValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /*--------------------------------------------------------HyperLogLog操作--------------------------------------------------------*/

    public void pfAdd(String key, Object... value) {
        redisTemplate.opsForHyperLogLog().add(key, value);
    }

    public long pfCount(String key) {
        return redisTemplate.opsForHyperLogLog().size(key);
    }

    /*----------------------------------------------------执行lua脚本-----------------------------------------------------------------*/
    public <R> R execute(Class<R> resultClass, String luaPath, List<String> keys, Object... args) {
        DefaultRedisScript<R> script = new DefaultRedisScript();
        script.setLocation(new ClassPathResource(luaPath));
        script.setResultType(resultClass);
        return redisTemplate.execute(script, keys, args);
    }

}
