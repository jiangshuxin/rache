package com.handpay.rache.core.spring.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastJSONRedisSerializer implements RedisSerializer<Object> {

	@Override
	public byte[] serialize(Object object) throws SerializationException {
		if (object == null) {
			return new byte[0];
		}
		return JSON.toJSONBytes(object, SerializerFeature.WriteDateUseDateFormat,SerializerFeature.WriteClassName);
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return JSON.parse(bytes);
	}
}
