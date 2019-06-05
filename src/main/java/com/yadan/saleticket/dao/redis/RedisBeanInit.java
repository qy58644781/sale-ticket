package com.yadan.saleticket.dao.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisBeanInit {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    @Bean
    public StringRedisTemplate stringRedisTemplate(){
        return new StringRedisTemplate(this.redisConnectionFactory);
    }
}
