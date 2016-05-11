package com.handpay.rache.core.spring;

import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCacheManagerX extends RedisCacheManager {
	public RedisCacheManagerX(RedisTemplate template) {
		super(template);
	}

}
