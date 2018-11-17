package com.yadan.saleticket.dao.redis;


import com.fasterxml.jackson.core.type.TypeReference;
import com.yadan.saleticket.base.exception.SystemException;
import com.yadan.saleticket.base.tools.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisHelper {
    private static Logger logger = LoggerFactory.getLogger(RedisHelper.class.getName());


    private static StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setStringRedisTemplate(@Autowired StringRedisTemplate stringRedisTemplate) {
        RedisHelper.stringRedisTemplate = stringRedisTemplate;
    }

    public static Object readValue(String key, Class<?> clazz) {
        String content = getFromRedis(key);

        if (StringUtils.isEmpty(content)) {
            return null;
        }

        Object value;
        try {
            value = Json.String2Object(content, clazz);
        } catch (Exception e) {
            logger.error("read Value is error", e);
            throw new SystemException("redis readValue error");
        }

        return value;
    }

    public static Object readValue(String key, TypeReference<?> valueTypeRef) {
        String content = getFromRedis(key);

        if (StringUtils.isEmpty(content)) {
            return null;
        }

        Object value;
        try {
            value = Json.String2Collection(content, valueTypeRef);
        } catch (Exception e) {
            logger.error("read TypeReferenceValue is error", e);
            throw new SystemException("redis read TypeReferenceValue error");
        }

        return value;
    }

    public static void writeValue(String key, Object value) {
        String content = writeValueAsString(value);
        setToRedis(key, content);
    }

    public static void writeValue(String key, Object value, long timeout) {
        String content = writeValueAsString(value);
        setToRedis(key, content, timeout);
    }

    public static Long getTimeOut(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    public static void delete(String key) {
        try {
            stringRedisTemplate.delete(key);
        } catch (Exception e) {
            logger.error("delete error", e);
            throw new SystemException("redis delete key error");
        }
    }

    public static boolean setIfAbsent(String key, String value) {
        boolean result;
        try {
            result = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            logger.error("setIfAbsent error", e);
            throw new SystemException("redis setIfAbsent error");
        }
        return result;
    }

    public static void writeIntoSortedSet(String key, String member, double score) {
        try {
            stringRedisTemplate.opsForZSet().add(key, member, score);
        } catch (Exception e) {
            logger.error("writeIntoSortedSet error", e);
            throw new SystemException("redis writeIntoSortedSet error");
        }
    }

    public static void deleteFromSortedSet(String key, Object... member) {
        try {
            stringRedisTemplate.opsForZSet().remove(key, member);
        } catch (Exception e) {
            logger.error("deleteFromSortedSet error", e);
            throw new SystemException("redis deleteFromSortedSet error");
        }
    }

    public static Set<String> readFromSortedSetRangeByScore(String key, double min, double max) {
        Set<String> result;
        try {
            result = stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
        } catch (Exception e) {
            logger.error("readFromSortedSetRangeByScore error", e);
            throw new SystemException("redis readFromSortedSetRangeByScore error");
        }
        return result;
    }

    private static String writeValueAsString(Object value) {
        String content;
        try {
            content = Json.Object2String(value);
        } catch (Exception e) {
            logger.error("把Object转换为String错误", e);
            throw new SystemException("redis writeValueAsString JsonProcessingException");
        }
        return content;
    }

    private static void setToRedis(String key, String content, long timeout) {
        try {
            stringRedisTemplate.opsForValue().set(key, content, timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("setToRedis error", e);
            throw new SystemException("setToRedis Exception");
        }
    }

    private static void setToRedis(String key, String content) {
        try {
            stringRedisTemplate.opsForValue().set(key, content);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("setToRedis error", e);
            throw new SystemException("setToRedis Exception");
        }
    }

    private static String getFromRedis(String key) {

        String content;
        try {
            content = stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.error("getFromRedis error", e);
            throw new SystemException("getFromRedis Exception");
        }

        if (!StringUtils.isEmpty(content)) {
            logger.info("from redis, key[" + key + "], value:" + content);
        }

        return content;
    }
}
