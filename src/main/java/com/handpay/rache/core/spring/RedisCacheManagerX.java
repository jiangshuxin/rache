package com.handpay.rache.core.spring;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.testng.collections.Maps;

import com.handpay.rache.core.spring.config.PropertyHolder;

public class RedisCacheManagerX extends RedisCacheManager {
	private StringRedisTemplateX stringRedisTemplateX;

	public RedisCacheManagerX(RedisTemplate template) {
		super(template);
	}

	@PostConstruct
	public void init(){
		Properties prop = PropertyHolder.getProperties();
		Set<Object> keySet = prop.keySet();
		Map<String, Long> expireMap = Maps.newHashMap();
		for(Object key : keySet){
			if(key == null) continue;
			String keyStr = key.toString();
			if(keyStr.startsWith("redis.cacheManager.expires.")){
				String oKey = StringUtils.substringAfterLast(keyStr, ".");
				Object value = prop.get(key);
				if(value == null) continue;
				if(NumberUtils.isNumber(value.toString())){
					expireMap.put(oKey, Long.parseLong(value.toString()));
				}
			}
		}
		if(expireMap.size() > 0){
			super.setExpires(expireMap);
			stringRedisTemplateX.setExpireMap(expireMap);
		}
	}

	public StringRedisTemplateX getStringRedisTemplateX() {
		return stringRedisTemplateX;
	}

	public void setStringRedisTemplateX(StringRedisTemplateX stringRedisTemplateX) {
		this.stringRedisTemplateX = stringRedisTemplateX;
	}
}
