package com.handpay.rache.core.spring.connection.impl;

import java.util.Arrays;
import java.util.Map;

import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.handpay.rache.core.spring.connection.StringRedisConnectionX;

public class DefaultStringRedisConnectionX extends DefaultStringRedisConnection implements StringRedisConnectionX {
	private Long defaultExpiration;
	private String defaultNamespace;
	private RedisSerializer<?> valueSerializer;
	private RedisSerializer<String> stringSerializer;
	private Map<String, Long> expireMap;
	private RedisCachePrefix cachePrefix = new DefaultRedisCachePrefix();

	public DefaultStringRedisConnectionX(RedisConnection connection, RedisSerializer<String> serializer) {
		super(connection, serializer);
	}

	public DefaultStringRedisConnectionX(RedisConnection connection) {
		super(connection);
	}

	@Override
	public void setEx(byte[] key, byte[] value) {
		super.setEx(key, getDefaultExpiration(), value);
	}

	@Override
	public void setEx(String key, String value) {
		super.setEx(key, getDefaultExpiration(), value);
	}

	@Override
	public void setObjEx(byte[] key, Object obj) {
		Long expire = extractExpireMapping(getDefaultNamespace());
		this.setObjEx(getDefaultNamespace(), key, obj, expire);
	}

	private byte[] extractKey(byte[] key, byte[] prefix) {
		if (prefix == null || prefix.length == 0){
			return key;
		}
		byte[] result = Arrays.copyOf(prefix, prefix.length + key.length);
		System.arraycopy(key, 0, result, prefix.length, key.length);
		return result;
	}

	@Override
	public void setObjEx(String key, Object obj) {
		Long expire = extractExpireMapping(getDefaultNamespace());
		this.setObjEx(getDefaultNamespace(), key, obj, expire);
	}

	@Override
	public void setObjEx(String nameSpace, String key, Object obj) {
		Long expire = extractExpireMapping(nameSpace);
		this.setObjEx(nameSpace, key, obj, expire);
	}

	@Override
	public void setObjEx(String nameSpace, byte[] key, Object obj) {
		Long expire = extractExpireMapping(nameSpace);
		this.setObjEx(nameSpace, key, obj, expire);
	}

	/**
	 * 根据nameSpace参数获取对应的过期时间，若不存在则返回默认过期时间
	 * @param nameSpace
	 * @return
	 */
	private Long extractExpireMapping(String nameSpace) {
		if(getExpireMap().containsKey(nameSpace)){
			return getExpireMap().get(nameSpace);
		}else{
			return getDefaultExpiration();
		}
	}

	@Override
	public void setObjEx(String nameSpace, String key, Object obj, Long expire) {
		byte[] prefix = cachePrefix.prefix(nameSpace);
		byte[] result = extractKey(getStringSerializer().serialize(key), prefix);
		super.setEx(getStringSerializer().deserialize(result), expire, getStringSerializer().deserialize(getValueSerializer().serialize(obj)));
	}

	@Override
	public void setObjEx(String nameSpace, byte[] key, Object obj, Long expire) {
		byte[] prefix = cachePrefix.prefix(nameSpace);
		byte[] result = extractKey(key, prefix);
		
		super.setEx(result, expire, getValueSerializer().serialize(obj));
	}

	@Override
	public void setObjOriginal(byte[] key, Object obj) {
		super.set(key, getValueSerializer().serialize(obj));
	}

	@Override
	public void setObjOriginal(String key, Object obj) {
		super.set(key, getStringSerializer().deserialize(getValueSerializer().serialize(obj)));
	}

	@Override
	public Object getObj(byte[] key) {
		return getObj(getDefaultNamespace(), key);
	}

	@Override
	public Object getObj(String key) {
		return getObj(getDefaultNamespace(), key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getObj(byte[] key, Class<T> clazz) {
		return (T)getObj(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getObj(String key, Class<T> clazz) {
		return (T)getObj(key);
	}

	@Override
	public Object getObj(String nameSpace, byte[] key) {
		byte[] prefix = cachePrefix.prefix(nameSpace);
		byte[] result = extractKey(key, prefix);
		return getValueSerializer().deserialize(super.get(result));
	}

	@Override
	public Object getObj(String nameSpace, String key) {
		byte[] prefix = cachePrefix.prefix(nameSpace);
		byte[] result = extractKey(getStringSerializer().serialize(key), prefix);
		return getValueSerializer().deserialize(super.get(result));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getObj(String nameSpace, byte[] key, Class<T> clazz) {
		return (T)getObj(nameSpace,key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getObj(String nameSpace, String key, Class<T> clazz) {
		return (T)getObj(nameSpace,key);
	}

	public Long getDefaultExpiration() {
		return defaultExpiration;
	}

	public void setDefaultExpiration(Long defaultExpiration) {
		this.defaultExpiration = defaultExpiration;
	}

	public RedisSerializer getValueSerializer() {
		return valueSerializer;
	}

	public void setValueSerializer(RedisSerializer<?> valueSerializer) {
		this.valueSerializer = valueSerializer;
	}

	public RedisSerializer<String> getStringSerializer() {
		return stringSerializer;
	}

	public void setStringSerializer(RedisSerializer<String> stringSerializer) {
		this.stringSerializer = stringSerializer;
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
