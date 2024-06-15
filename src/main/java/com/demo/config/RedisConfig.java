package com.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
        poolConfig.setMinIdle(redisProperties.getJedis().getPool().getMaxIdle());
        poolConfig.setTestOnBorrow(false);
        poolConfig.setTestWhileIdle(false);
        return poolConfig;
    }

    @Bean
    public JedisConnectionFactory jedisCf(@Autowired JedisPoolConfig jedisPoolConfig) {
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        standaloneConfiguration.setPassword(redisProperties.getPassword());

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
                JedisClientConfiguration.builder()
                        .usePooling();
        jpcb.poolConfig(jedisPoolConfig);
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();


        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(standaloneConfiguration, jedisClientConfiguration);
        return jedisConFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(@Autowired JedisConnectionFactory cf) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(cf);
        return template;
    }
}
