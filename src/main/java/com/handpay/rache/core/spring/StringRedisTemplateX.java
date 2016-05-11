package com.handpay.rache.core.spring;

import java.util.Map;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.handpay.rache.core.spring.connection.impl.DefaultStringRedisConnectionX;

/**
 * RedisTemplate实用型扩展类
 * <li>推荐使用实用型扩展接口StringRedisConnectionX进行回调方法的逻辑编写
 * <li>根据命名空间维度指定过期时间
 * <li>具有StringRedisTemplate的一切功能
 * @see com.handpay.rache.core.spring.connection.StringRedisConnectionX
 * @see org.springframework.data.redis.core.StringRedisTemplate
 * @author sxjiang
 *
 */
public class StringRedisTemplateX extends StringRedisTemplate {
	private Long defaultExpiration;
	private String defaultNamespace;
	private Map<String, Long> expireMap;

	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		DefaultStringRedisConnectionX conn = new DefaultStringRedisConnectionX((StringRedisConnection)super.preProcessConnection(connection, existingConnection));
		conn.setDefaultExpiration(getDefaultExpiration());
		conn.setStringSerializer(getStringSerializer());
		conn.setValueSerializer(getDefaultSerializer());
		conn.setDefaultNamespace(getDefaultNamespace());
		conn.setExpireMap(getExpireMap());
		return conn;
	}

	public Long getDefaultExpiration() {
		return defaultExpiration;
	}

	public void setDefaultExpiration(Long defaultExpiration) {
		this.defaultExpiration = defaultExpiration;
	}

	public String getDefaultNamespace() {
		return defaultNamespace;
	}

	public void setDefaultNamespace(String defaultNamespace) {
		this.defaultNamespace = defaultNamespace;
	}

	public Map<String, Long> getExpireMap() {
		return expireMap;
	}

	public void setExpireMap(Map<String, Long> expireMap) {
		this.expireMap = expireMap;
	}
}
