package com.storage.config;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.storage.utils.jedis.JedisClientPool;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

@Component
@PropertySource("classpath:myapp.properties")
public class RedisConfig {

@Value("${redis.address}")
String url;
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
	    RedisTemplate<String, Object> template = new RedisTemplate<>();
	    template.setConnectionFactory(jedisConnectionFactory()); 
	    return template;
	}
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration configuration=new RedisStandaloneConfiguration(url, 6379);
		JedisConnectionFactory jedisConFactory
	      = new JedisConnectionFactory(configuration);
	    return jedisConFactory;
	}
	public JedisPool jedis() {
		JedisPool jedisPool=new JedisPool(new GenericObjectPoolConfig(),
				url, 6379,Protocol.DEFAULT_TIMEOUT);
		return jedisPool;
	}
	
	@Bean
	public JedisClientPool jedisClientPool() {
		JedisClientPool jedisPool=new JedisClientPool();
		jedisPool.setJedisPool(jedis());
		return jedisPool;
	}
	
	
}
