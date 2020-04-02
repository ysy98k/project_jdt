package com.raising.cache;

import com.raising.cache.MybatisRedisCache;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * mybatis redis 二级缓存参数传递
 *
 * @author seer
 * @date 2018/3/13 14:17
 */
public class MybatisRedisCacheTransfer {
    public void setJedisConnectionFactory(JedisConnectionFactory jedisConnectionFactory) {
        MybatisRedisCache.setJedisConnectionFactory(jedisConnectionFactory);
    }
}