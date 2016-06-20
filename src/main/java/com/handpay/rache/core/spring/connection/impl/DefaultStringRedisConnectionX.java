package com.handpay.rache.core.spring.connection.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
	public Long append(byte[] key, byte[] value) {
		return super.append(extractKey(key, extractPrefix(getDefaultNamespace())), value);
	}

	@Override
	public List<byte[]> bLPop(int timeout, byte[]... keys) {
		return super.bLPop(timeout, extractKeyList(keys));
	}

	@Override
	public List<byte[]> bRPop(int timeout, byte[]... keys) {
		return super.bRPop(timeout, extractKeyList(keys));
	}

	private byte[][] extractKeyList(byte[]... keys) {
		List<byte[]> newKeys = Lists.newArrayList();
		for(byte[] key : keys){
			byte[] prefix = extractPrefix(getDefaultNamespace());
			newKeys.add(extractKey(key, prefix));
		}
		return newKeys.toArray(new byte[0][0]);
	}

	@Override
	public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
		byte[] prefix = extractPrefix(getDefaultNamespace());
		return super.bRPopLPush(timeout, extractKey(srcKey, prefix), extractKey(dstKey, prefix));
	}

	@Override
	public Long decr(byte[] key) {
		return super.decr(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Long decrBy(byte[] key, long value) {
		return super.decrBy(extractKey(key, extractPrefix(getDefaultNamespace())), value);
	}

	@Override
	public Long del(byte[]... keys) {
		return super.del(extractKeyList(keys));
	}

	@Override
	public Boolean exists(byte[] key) {
		return super.exists(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Boolean expire(byte[] key, long seconds) {
		return super.expire(extractKey(key, extractPrefix(getDefaultNamespace())), seconds);
	}

	@Override
	public Boolean expireAt(byte[] key, long unixTime) {
		return super.expireAt(extractKey(key, extractPrefix(getDefaultNamespace())), unixTime);
	}

	@Override
	public byte[] get(byte[] key) {
		return super.get(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Boolean getBit(byte[] key, long offset) {
		return super.getBit(extractKey(key, extractPrefix(getDefaultNamespace())), offset);
	}

	@Override
	public byte[] getRange(byte[] key, long start, long end) {
		return super.getRange(extractKey(key, extractPrefix(getDefaultNamespace())), start, end);
	}

	@Override
	public byte[] getSet(byte[] key, byte[] value) {
		return super.getSet(extractKey(key, extractPrefix(getDefaultNamespace())), value);
	}

	@Override
	public Long hDel(byte[] key, byte[]... fields) {
		return super.hDel(extractKey(key, extractPrefix(getDefaultNamespace())), fields);
	}

	@Override
	public Boolean hExists(byte[] key, byte[] field) {
		return super.hExists(extractKey(key, extractPrefix(getDefaultNamespace())), field);
	}

	@Override
	public byte[] hGet(byte[] key, byte[] field) {
		return super.hGet(extractKey(key, extractPrefix(getDefaultNamespace())), field);
	}

	@Override
	public Map<byte[], byte[]> hGetAll(byte[] key) {
		return super.hGetAll(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Long hIncrBy(byte[] key, byte[] field, long delta) {
		return super.hIncrBy(extractKey(key, extractPrefix(getDefaultNamespace())), field, delta);
	}

	@Override
	public Double hIncrBy(byte[] key, byte[] field, double delta) {
		return super.hIncrBy(extractKey(key, extractPrefix(getDefaultNamespace())), field, delta);
	}

	@Override
	public Set<byte[]> hKeys(byte[] key) {
		return super.hKeys(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Long hLen(byte[] key) {
		return super.hLen(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public List<byte[]> hMGet(byte[] key, byte[]... fields) {
		return super.hMGet(extractKey(key, extractPrefix(getDefaultNamespace())), fields);
	}

	@Override
	public void hMSet(byte[] key, Map<byte[], byte[]> hashes) {
		super.hMSet(extractKey(key, extractPrefix(getDefaultNamespace())), hashes);
	}

	@Override
	public Boolean hSet(byte[] key, byte[] field, byte[] value) {
		return super.hSet(extractKey(key, extractPrefix(getDefaultNamespace())), field, value);
	}

	@Override
	public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
		return super.hSetNX(extractKey(key, extractPrefix(getDefaultNamespace())), field, value);
	}

	@Override
	public List<byte[]> hVals(byte[] key) {
		return super.hVals(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Long incr(byte[] key) {
		return super.incr(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Long incrBy(byte[] key, long value) {
		return super.incrBy(extractKey(key, extractPrefix(getDefaultNamespace())), value);
	}

	@Override
	public Double incrBy(byte[] key, double value) {
		return super.incrBy(extractKey(key, extractPrefix(getDefaultNamespace())), value);
	}

	@Override
	public Set<byte[]> keys(byte[] pattern) {
		return super.keys(extractKey(pattern, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public byte[] lIndex(byte[] key, long index) {
		return super.lIndex(extractKey(key, extractPrefix(getDefaultNamespace())), index);
	}

	@Override
	public Long lInsert(byte[] key, Position where, byte[] pivot, byte[] value) {
		return super.lInsert(extractKey(key, extractPrefix(getDefaultNamespace())), where, pivot, value);
	}

	@Override
	public Long lLen(byte[] key) {
		return super.lLen(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public byte[] lPop(byte[] key) {
		return super.lPop(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Long lPush(byte[] key, byte[]... values) {
		return super.lPush(extractKey(key, extractPrefix(getDefaultNamespace())), values);
	}

	@Override
	public Long lPushX(byte[] key, byte[] value) {
		return super.lPushX(extractKey(key, extractPrefix(getDefaultNamespace())), value);
	}

	@Override
	public List<byte[]> lRange(byte[] key, long start, long end) {
		return super.lRange(extractKey(key, extractPrefix(getDefaultNamespace())), start, end);
	}

	@Override
	public Long lRem(byte[] key, long count, byte[] value) {
		return super.lRem(extractKey(key, extractPrefix(getDefaultNamespace())), count, value);
	}

	@Override
	public void lSet(byte[] key, long index, byte[] value) {
		super.lSet(extractKey(key, extractPrefix(getDefaultNamespace())), index, value);
	}

	@Override
	public void lTrim(byte[] key, long start, long end) {
		super.lTrim(extractKey(key, extractPrefix(getDefaultNamespace())), start, end);
	}

	@Override
	public List<byte[]> mGet(byte[]... keys) {
		return super.mGet(extractKeyList(keys));
	}

	@Override
	public void mSet(Map<byte[], byte[]> tuple) {
		Map<byte[], byte[]> map = extractKeyMap(tuple);
		super.mSet(map);
	}

	@Override
	public Boolean mSetNX(Map<byte[], byte[]> tuple) {
		Map<byte[], byte[]> map = extractKeyMap(tuple);
		return super.mSetNX(map);
	}

	private Map<byte[], byte[]> extractKeyMap(Map<byte[], byte[]> tuple) {
		Map<byte[],byte[]> map = Maps.newHashMap();
		for(byte[] key : tuple.keySet()){
			map.put(extractKey(key, extractPrefix(getDefaultNamespace())), tuple.get(key));
		}
		return map;
	}

	@Override
	public Boolean persist(byte[] key) {
		return super.persist(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Boolean move(byte[] key, int dbIndex) {
		return super.move(extractKey(key, extractPrefix(getDefaultNamespace())), dbIndex);
	}

	@Override
	public void pSubscribe(MessageListener listener, byte[]... patterns) {
		super.pSubscribe(listener, extractKeyList(patterns));
	}

	@Override
	public Long publish(byte[] channel, byte[] message) {
		return super.publish(channel, message);
	}

	@Override
	public void rename(byte[] oldName, byte[] newName) {
		super.rename(extractKey(oldName, extractPrefix(getDefaultNamespace())), extractKey(newName, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Boolean renameNX(byte[] oldName, byte[] newName) {
		return super.renameNX(extractKey(oldName, extractPrefix(getDefaultNamespace())), extractKey(newName, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public byte[] rPop(byte[] key) {
		return super.rPop(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
		return super.rPopLPush(extractKey(srcKey, extractPrefix(getDefaultNamespace())), extractKey(dstKey, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Long rPush(byte[] key, byte[]... values) {
		return super.rPush(extractKey(key, extractPrefix(getDefaultNamespace())), values);
	}

	@Override
	public Long rPushX(byte[] key, byte[] value) {
		return super.rPushX(extractKey(key, extractPrefix(getDefaultNamespace())), value);
	}

	@Override
	public Long sAdd(byte[] key, byte[]... values) {
		return super.sAdd(extractKey(key, extractPrefix(getDefaultNamespace())), values);
	}

	@Override
	public Long sCard(byte[] key) {
		return super.sCard(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Set<byte[]> sDiff(byte[]... keys) {
		return super.sDiff(extractKeyList(keys));
	}

	@Override
	public Long sDiffStore(byte[] destKey, byte[]... keys) {
		return super.sDiffStore(extractKey(destKey, extractPrefix(getDefaultNamespace())), extractKeyList(keys));
	}

	@Override
	public void set(byte[] key, byte[] value) {
		super.set(extractKey(key, extractPrefix(getDefaultNamespace())), value);
	}

	@Override
	public void setBit(byte[] key, long offset, boolean value) {
		super.setBit(extractKey(key, extractPrefix(getDefaultNamespace())), offset, value);
	}

	@Override
	public void setEx(byte[] key, long seconds, byte[] value) {
		super.setEx(extractKey(key, extractPrefix(getDefaultNamespace())), seconds, value);
	}

	@Override
	public Boolean setNX(byte[] key, byte[] value) {
		return super.setNX(extractKey(key, extractPrefix(getDefaultNamespace())), value);
	}

	@Override
	public void setRange(byte[] key, byte[] value, long start) {
		super.setRange(extractKey(key, extractPrefix(getDefaultNamespace())), value, start);
	}

	@Override
	public Set<byte[]> sInter(byte[]... keys) {
		return super.sInter(extractKeyList(keys));
	}

	@Override
	public Long sInterStore(byte[] destKey, byte[]... keys) {
		return super.sInterStore(extractKey(destKey, extractPrefix(getDefaultNamespace())), extractKeyList(keys));
	}

	@Override
	public Boolean sIsMember(byte[] key, byte[] value) {
		return super.sIsMember(extractKey(key, extractPrefix(getDefaultNamespace())), value);
	}

	@Override
	public Set<byte[]> sMembers(byte[] key) {
		return super.sMembers(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
		return super.sMove(extractKey(srcKey, extractPrefix(getDefaultNamespace())), extractKey(destKey, extractPrefix(getDefaultNamespace())), value);
	}

	@Override
	public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
		return super.sort(extractKey(key, extractPrefix(getDefaultNamespace())), params, extractKey(storeKey, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public List<byte[]> sort(byte[] key, SortParameters params) {
		return super.sort(extractKey(key, extractPrefix(getDefaultNamespace())), params);
	}

	@Override
	public byte[] sPop(byte[] key) {
		return super.sPop(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public byte[] sRandMember(byte[] key) {
		return super.sRandMember(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public List<byte[]> sRandMember(byte[] key, long count) {
		return super.sRandMember(extractKey(key, extractPrefix(getDefaultNamespace())), count);
	}

	@Override
	public Long sRem(byte[] key, byte[]... values) {
		return super.sRem(extractKey(key, extractPrefix(getDefaultNamespace())), values);
	}

	@Override
	public Long strLen(byte[] key) {
		return super.strLen(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Long bitCount(byte[] key) {
		return super.bitCount(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Long bitCount(byte[] key, long begin, long end) {
		return super.bitCount(extractKey(key, extractPrefix(getDefaultNamespace())), begin, end);
	}

	@Override
	public Long bitOp(BitOperation op, byte[] destination, byte[]... keys) {
		return super.bitOp(op, destination, extractKeyList(keys));
	}

	@Override
	public void subscribe(MessageListener listener, byte[]... channels) {
		super.subscribe(listener, channels);
	}

	@Override
	public Set<byte[]> sUnion(byte[]... keys) {
		return super.sUnion(extractKeyList(keys));
	}

	@Override
	public Long sUnionStore(byte[] destKey, byte[]... keys) {
		return super.sUnionStore(extractKey(destKey, extractPrefix(getDefaultNamespace())), extractKeyList(keys));
	}

	@Override
	public Long ttl(byte[] key) {
		return super.ttl(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public DataType type(byte[] key) {
		return super.type(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public void watch(byte[]... keys) {
		super.watch(extractKeyList(keys));
	}

	@Override
	public Boolean zAdd(byte[] key, double score, byte[] value) {
		return super.zAdd(extractKey(key, extractPrefix(getDefaultNamespace())), score, value);
	}

	@Override
	public Long zAdd(byte[] key, Set<Tuple> tuples) {
		return super.zAdd(extractKey(key, extractPrefix(getDefaultNamespace())), tuples);
	}

	@Override
	public Long zCard(byte[] key) {
		return super.zCard(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public Long zCount(byte[] key, double min, double max) {
		return super.zCount(extractKey(key, extractPrefix(getDefaultNamespace())), min, max);
	}

	@Override
	public Double zIncrBy(byte[] key, double increment, byte[] value) {
		return super.zIncrBy(extractKey(key, extractPrefix(getDefaultNamespace())), increment, value);
	}

	@Override
	public Long zInterStore(byte[] destKey, Aggregate aggregate, int[] weights, byte[]... sets) {
		return super.zInterStore(extractKey(destKey, extractPrefix(getDefaultNamespace())), aggregate, weights, sets);
	}

	@Override
	public Long zInterStore(byte[] destKey, byte[]... sets) {
		return super.zInterStore(extractKey(destKey, extractPrefix(getDefaultNamespace())), sets);
	}

	@Override
	public Set<byte[]> zRange(byte[] key, long start, long end) {
		return super.zRange(extractKey(key, extractPrefix(getDefaultNamespace())), start, end);
	}

	@Override
	public Set<byte[]> zRangeByScore(byte[] key, double min, double max, long offset, long count) {
		return super.zRangeByScore(extractKey(key, extractPrefix(getDefaultNamespace())), min, max, offset, count);
	}

	@Override
	public Set<byte[]> zRangeByScore(byte[] key, double min, double max) {
		return super.zRangeByScore(extractKey(key, extractPrefix(getDefaultNamespace())), min, max);
	}

	@Override
	public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
		return super.zRangeByScoreWithScores(extractKey(key, extractPrefix(getDefaultNamespace())), min, max, offset, count);
	}

	@Override
	public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max) {
		return super.zRangeByScoreWithScores(extractKey(key, extractPrefix(getDefaultNamespace())), min, max);
	}

	@Override
	public Set<Tuple> zRangeWithScores(byte[] key, long start, long end) {
		return super.zRangeWithScores(extractKey(key, extractPrefix(getDefaultNamespace())), start, end);
	}

	@Override
	public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max, long offset, long count) {
		return super.zRevRangeByScore(extractKey(key, extractPrefix(getDefaultNamespace())), min, max, offset, count);
	}

	@Override
	public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max) {
		return super.zRevRangeByScore(extractKey(key, extractPrefix(getDefaultNamespace())), min, max);
	}

	@Override
	public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
		return super.zRevRangeByScoreWithScores(extractKey(key, extractPrefix(getDefaultNamespace())), min, max, offset, count);
	}

	@Override
	public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
		return super.zRevRangeByScoreWithScores(extractKey(key, extractPrefix(getDefaultNamespace())), min, max);
	}

	@Override
	public Long zRank(byte[] key, byte[] value) {
		return super.zRank(extractKey(key, extractPrefix(getDefaultNamespace())), value);
	}

	@Override
	public Long zRem(byte[] key, byte[]... values) {
		return super.zRem(extractKey(key, extractPrefix(getDefaultNamespace())), values);
	}

	@Override
	public Long zRemRange(byte[] key, long start, long end) {
		return super.zRemRange(extractKey(key, extractPrefix(getDefaultNamespace())), start, end);
	}

	@Override
	public Long zRemRangeByScore(byte[] key, double min, double max) {
		return super.zRemRangeByScore(extractKey(key, extractPrefix(getDefaultNamespace())), min, max);
	}

	@Override
	public Set<byte[]> zRevRange(byte[] key, long start, long end) {
		return super.zRevRange(extractKey(key, extractPrefix(getDefaultNamespace())), start, end);
	}

	@Override
	public Set<Tuple> zRevRangeWithScores(byte[] key, long start, long end) {
		return super.zRevRangeWithScores(extractKey(key, extractPrefix(getDefaultNamespace())), start, end);
	}

	@Override
	public Long zRevRank(byte[] key, byte[] value) {
		return super.zRevRank(extractKey(key, extractPrefix(getDefaultNamespace())), value);
	}

	@Override
	public Double zScore(byte[] key, byte[] value) {
		return super.zScore(extractKey(key, extractPrefix(getDefaultNamespace())), value);
	}

	@Override
	public Long zUnionStore(byte[] destKey, Aggregate aggregate, int[] weights, byte[]... sets) {
		return super.zUnionStore(extractKey(destKey, extractPrefix(getDefaultNamespace())), aggregate, weights, sets);
	}

	@Override
	public Long zUnionStore(byte[] destKey, byte[]... sets) {
		return super.zUnionStore(extractKey(destKey, extractPrefix(getDefaultNamespace())), sets);
	}

	@Override
	public Boolean pExpire(byte[] key, long millis) {
		return super.pExpire(extractKey(key, extractPrefix(getDefaultNamespace())), millis);
	}

	@Override
	public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
		return super.pExpireAt(extractKey(key, extractPrefix(getDefaultNamespace())), unixTimeInMillis);
	}

	@Override
	public Long pTtl(byte[] key) {
		return super.pTtl(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public byte[] dump(byte[] key) {
		return super.dump(extractKey(key, extractPrefix(getDefaultNamespace())));
	}

	@Override
	public void restore(byte[] key, long ttlInMillis, byte[] serializedValue) {
		super.restore(extractKey(key, extractPrefix(getDefaultNamespace())), ttlInMillis, serializedValue);
	}

	@Override
	public <T> T eval(byte[] script, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
		return super.eval(script, returnType, numKeys, keysAndArgs);
	}

	@Override
	public <T> T evalSha(String scriptSha1, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
		return super.evalSha(scriptSha1, returnType, numKeys, keysAndArgs);
	}

	@Override
	public Long append(String key, String value) {
		return super.append(extractKey(key, extractPrefixStr(getDefaultNamespace())), value);
	}

	@Override
	public List<String> bLPop(int timeout, String... keys) {
		return super.bLPop(timeout, extractKeyList(keys));
	}

	private String[] extractKeyList(String... keys) {
		List<String> list = Lists.newArrayList();
		for(String key : keys){
			list.add(extractKey(key, extractPrefixStr(getDefaultNamespace())));
		}
		String[] keyArr = list.toArray(new String[0]);
		return keyArr;
	}

	@Override
	public List<String> bRPop(int timeout, String... keys) {
		return super.bRPop(timeout, extractKeyList(keys));
	}

	@Override
	public String bRPopLPush(int timeout, String srcKey, String dstKey) {
		return super.bRPopLPush(timeout, extractKey(srcKey, extractPrefixStr(getDefaultNamespace())), extractKey(dstKey, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Long decr(String key) {
		return super.decr(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Long decrBy(String key, long value) {
		return super.decrBy(extractKey(key, extractPrefixStr(getDefaultNamespace())), value);
	}

	@Override
	public Long del(String... keys) {
		return super.del(extractKeyList(keys));
	}

	@Override
	public String echo(String message) {
		return super.echo(message);
	}

	@Override
	public Boolean exists(String key) {
		return super.exists(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Boolean expire(String key, long seconds) {
		return super.expire(extractKey(key, extractPrefixStr(getDefaultNamespace())), seconds);
	}

	@Override
	public Boolean expireAt(String key, long unixTime) {
		return super.expireAt(extractKey(key, extractPrefixStr(getDefaultNamespace())), unixTime);
	}

	@Override
	public String get(String key) {
		return super.get(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Boolean getBit(String key, long offset) {
		return super.getBit(extractKey(key, extractPrefixStr(getDefaultNamespace())), offset);
	}

	@Override
	public String getRange(String key, long start, long end) {
		return super.getRange(extractKey(key, extractPrefixStr(getDefaultNamespace())), start, end);
	}

	@Override
	public String getSet(String key, String value) {
		return super.getSet(extractKey(key, extractPrefixStr(getDefaultNamespace())), value);
	}

	@Override
	public Long hDel(String key, String... fields) {
		return super.hDel(extractKey(key, extractPrefixStr(getDefaultNamespace())), fields);
	}

	@Override
	public Boolean hExists(String key, String field) {
		return super.hExists(extractKey(key, extractPrefixStr(getDefaultNamespace())), field);
	}

	@Override
	public String hGet(String key, String field) {
		return super.hGet(extractKey(key, extractPrefixStr(getDefaultNamespace())), field);
	}

	@Override
	public Map<String, String> hGetAll(String key) {
		return super.hGetAll(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Long hIncrBy(String key, String field, long delta) {
		return super.hIncrBy(extractKey(key, extractPrefixStr(getDefaultNamespace())), field, delta);
	}

	@Override
	public Double hIncrBy(String key, String field, double delta) {
		return super.hIncrBy(extractKey(key, extractPrefixStr(getDefaultNamespace())), field, delta);
	}

	@Override
	public Set<String> hKeys(String key) {
		return super.hKeys(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Long hLen(String key) {
		return super.hLen(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public List<String> hMGet(String key, String... fields) {
		return super.hMGet(extractKey(key, extractPrefixStr(getDefaultNamespace())), fields);
	}

	@Override
	public void hMSet(String key, Map<String, String> hashes) {
		super.hMSet(extractKey(key, extractPrefixStr(getDefaultNamespace())), hashes);
	}

	@Override
	public Boolean hSet(String key, String field, String value) {
		return super.hSet(extractKey(key, extractPrefixStr(getDefaultNamespace())), field, value);
	}

	@Override
	public Boolean hSetNX(String key, String field, String value) {
		return super.hSetNX(extractKey(key, extractPrefixStr(getDefaultNamespace())), field, value);
	}

	@Override
	public List<String> hVals(String key) {
		return super.hVals(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Long incr(String key) {
		return super.incr(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Long incrBy(String key, long value) {
		return super.incrBy(extractKey(key, extractPrefixStr(getDefaultNamespace())), value);
	}

	@Override
	public Double incrBy(String key, double value) {
		return super.incrBy(extractKey(key, extractPrefixStr(getDefaultNamespace())), value);
	}

	@Override
	public Collection<String> keys(String pattern) {
		return super.keys(extractKey(pattern, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public String lIndex(String key, long index) {
		return super.lIndex(extractKey(key, extractPrefixStr(getDefaultNamespace())), index);
	}

	@Override
	public Long lInsert(String key, Position where, String pivot, String value) {
		return super.lInsert(extractKey(key, extractPrefixStr(getDefaultNamespace())), where, pivot, value);
	}

	@Override
	public Long lLen(String key) {
		return super.lLen(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public String lPop(String key) {
		return super.lPop(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Long lPush(String key, String... values) {
		return super.lPush(extractKey(key, extractPrefixStr(getDefaultNamespace())), values);
	}

	@Override
	public Long lPushX(String key, String value) {
		return super.lPushX(extractKey(key, extractPrefixStr(getDefaultNamespace())), value);
	}

	@Override
	public List<String> lRange(String key, long start, long end) {
		return super.lRange(extractKey(key, extractPrefixStr(getDefaultNamespace())), start, end);
	}

	@Override
	public Long lRem(String key, long count, String value) {
		return super.lRem(extractKey(key, extractPrefixStr(getDefaultNamespace())), count, value);
	}

	@Override
	public void lSet(String key, long index, String value) {
		super.lSet(extractKey(key, extractPrefixStr(getDefaultNamespace())), index, value);
	}

	@Override
	public void lTrim(String key, long start, long end) {
		super.lTrim(extractKey(key, extractPrefixStr(getDefaultNamespace())), start, end);
	}

	@Override
	public List<String> mGet(String... keys) {
		return super.mGet(extractKeyList(keys));
	}

	@Override
	public Boolean mSetNXString(Map<String, String> tuple) {
		// TODO Auto-generated method stub
		return super.mSetNXString(tuple);
	}

	@Override
	public void mSetString(Map<String, String> tuple) {
		// TODO Auto-generated method stub
		super.mSetString(tuple);
	}

	@Override
	public Boolean persist(String key) {
		return super.persist(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Boolean move(String key, int dbIndex) {
		return super.move(extractKey(key, extractPrefixStr(getDefaultNamespace())), dbIndex);
	}

	@Override
	public void pSubscribe(MessageListener listener, String... patterns) {
		// TODO Auto-generated method stub
		super.pSubscribe(listener, patterns);
	}

	@Override
	public Long publish(String channel, String message) {
		return super.publish(channel, message);
	}

	@Override
	public void rename(String oldName, String newName) {
		super.rename(extractKey(oldName, extractPrefixStr(getDefaultNamespace())), extractKey(newName, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Boolean renameNX(String oldName, String newName) {
		return super.renameNX(extractKey(oldName, extractPrefixStr(getDefaultNamespace())), extractKey(newName, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public String rPop(String key) {
		return super.rPop(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public String rPopLPush(String srcKey, String dstKey) {
		return super.rPopLPush(extractKey(srcKey, extractPrefixStr(getDefaultNamespace())), extractKey(dstKey, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Long rPush(String key, String... values) {
		return super.rPush(extractKey(key, extractPrefixStr(getDefaultNamespace())), values);
	}

	@Override
	public Long rPushX(String key, String value) {
		return super.rPushX(extractKey(key, extractPrefixStr(getDefaultNamespace())), value);
	}

	@Override
	public Long sAdd(String key, String... values) {
		return super.sAdd(extractKey(key, extractPrefixStr(getDefaultNamespace())), values);
	}

	@Override
	public Long sCard(String key) {
		return super.sCard(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Set<String> sDiff(String... keys) {
		return super.sDiff(extractKeyList(keys));
	}

	@Override
	public Long sDiffStore(String destKey, String... keys) {
		return super.sDiffStore(extractKey(destKey, extractPrefixStr(getDefaultNamespace())), extractKeyList(keys));
	}

	@Override
	public void set(String key, String value) {
		super.set(extractKey(key, extractPrefixStr(getDefaultNamespace())), value);
	}

	@Override
	public void setBit(String key, long offset, boolean value) {
		super.setBit(extractKey(key, extractPrefixStr(getDefaultNamespace())), offset, value);
	}

	@Override
	public void setEx(String key, long seconds, String value) {
		super.setEx(extractKey(key, extractPrefixStr(getDefaultNamespace())), seconds, value);
	}

	@Override
	public Boolean setNX(String key, String value) {
		return super.setNX(extractKey(key, extractPrefixStr(getDefaultNamespace())), value);
	}

	@Override
	public void setRange(String key, String value, long start) {
		super.setRange(extractKey(key, extractPrefixStr(getDefaultNamespace())), value, start);
	}

	@Override
	public Set<String> sInter(String... keys) {
		return super.sInter(extractKeyList(keys));
	}

	@Override
	public Long sInterStore(String destKey, String... keys) {
		return super.sInterStore(extractKey(destKey, extractPrefixStr(getDefaultNamespace())), extractKeyList(keys));
	}

	@Override
	public Boolean sIsMember(String key, String value) {
		return super.sIsMember(extractKey(key, extractPrefixStr(getDefaultNamespace())), value);
	}

	@Override
	public Set<String> sMembers(String key) {
		return super.sMembers(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Boolean sMove(String srcKey, String destKey, String value) {
		return super.sMove(extractKey(srcKey, extractPrefixStr(getDefaultNamespace())), extractKey(destKey, extractPrefixStr(getDefaultNamespace())), value);
	}

	@Override
	public Long sort(String key, SortParameters params, String storeKey) {
		return super.sort(extractKey(key, extractPrefixStr(getDefaultNamespace())), params, storeKey);
	}

	@Override
	public List<String> sort(String key, SortParameters params) {
		return super.sort(extractKey(key, extractPrefixStr(getDefaultNamespace())), params);
	}

	@Override
	public String sPop(String key) {
		return super.sPop(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public String sRandMember(String key) {
		return super.sRandMember(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public List<String> sRandMember(String key, long count) {
		return super.sRandMember(extractKey(key, extractPrefixStr(getDefaultNamespace())), count);
	}

	@Override
	public Long sRem(String key, String... values) {
		return super.sRem(extractKey(key, extractPrefixStr(getDefaultNamespace())), values);
	}

	@Override
	public Long strLen(String key) {
		return super.strLen(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Long bitCount(String key) {
		return super.bitCount(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Long bitCount(String key, long begin, long end) {
		return super.bitCount(extractKey(key, extractPrefixStr(getDefaultNamespace())), begin, end);
	}

	@Override
	public Long bitOp(BitOperation op, String destination, String... keys) {
		return super.bitOp(op, destination, extractKeyList(keys));
	}

	@Override
	public void subscribe(MessageListener listener, String... channels) {
		// TODO Auto-generated method stub
		super.subscribe(listener, channels);
	}

	@Override
	public Set<String> sUnion(String... keys) {
		return super.sUnion(extractKeyList(keys));
	}

	@Override
	public Long sUnionStore(String destKey, String... keys) {
		return super.sUnionStore(extractKey(destKey, extractPrefixStr(getDefaultNamespace())), extractKeyList(keys));
	}

	@Override
	public Long ttl(String key) {
		return super.ttl(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public DataType type(String key) {
		return super.type(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Boolean zAdd(String key, double score, String value) {
		return super.zAdd(extractKey(key, extractPrefixStr(getDefaultNamespace())), score, value);
	}

	@Override
	public Long zAdd(String key, Set<StringTuple> tuples) {
		return super.zAdd(extractKey(key, extractPrefixStr(getDefaultNamespace())), tuples);
	}

	@Override
	public Long zCard(String key) {
		return super.zCard(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public Long zCount(String key, double min, double max) {
		return super.zCount(extractKey(key, extractPrefixStr(getDefaultNamespace())), min, max);
	}

	@Override
	public Double zIncrBy(String key, double increment, String value) {
		return super.zIncrBy(extractKey(key, extractPrefixStr(getDefaultNamespace())), increment, value);
	}

	@Override
	public Long zInterStore(String destKey, Aggregate aggregate, int[] weights, String... sets) {
		return super.zInterStore(extractKey(destKey, extractPrefixStr(getDefaultNamespace())), aggregate, weights, sets);
	}

	@Override
	public Long zInterStore(String destKey, String... sets) {
		return super.zInterStore(extractKey(destKey, extractPrefixStr(getDefaultNamespace())), sets);
	}

	@Override
	public Set<String> zRange(String key, long start, long end) {
		return super.zRange(extractKey(key, extractPrefixStr(getDefaultNamespace())), start, end);
	}

	@Override
	public Set<String> zRangeByScore(String key, double min, double max, long offset, long count) {
		return super.zRangeByScore(extractKey(key, extractPrefixStr(getDefaultNamespace())), min, max, offset, count);
	}

	@Override
	public Set<String> zRangeByScore(String key, double min, double max) {
		return super.zRangeByScore(extractKey(key, extractPrefixStr(getDefaultNamespace())), min, max);
	}

	@Override
	public Set<StringTuple> zRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
		return super.zRangeByScoreWithScores(extractKey(key, extractPrefixStr(getDefaultNamespace())), min, max, offset, count);
	}

	@Override
	public Set<StringTuple> zRangeByScoreWithScores(String key, double min, double max) {
		return super.zRangeByScoreWithScores(extractKey(key, extractPrefixStr(getDefaultNamespace())), min, max);
	}

	@Override
	public Set<StringTuple> zRangeWithScores(String key, long start, long end) {
		return super.zRangeWithScores(extractKey(key, extractPrefixStr(getDefaultNamespace())), start, end);
	}

	@Override
	public Long zRank(String key, String value) {
		return super.zRank(extractKey(key, extractPrefixStr(getDefaultNamespace())), value);
	}

	@Override
	public Long zRem(String key, String... values) {
		return super.zRem(extractKey(key, extractPrefixStr(getDefaultNamespace())), values);
	}

	@Override
	public Long zRemRange(String key, long start, long end) {
		return super.zRemRange(extractKey(key, extractPrefixStr(getDefaultNamespace())), start, end);
	}

	@Override
	public Long zRemRangeByScore(String key, double min, double max) {
		return super.zRemRangeByScore(extractKey(key, extractPrefixStr(getDefaultNamespace())), min, max);
	}

	@Override
	public Set<String> zRevRange(String key, long start, long end) {
		return super.zRevRange(extractKey(key, extractPrefixStr(getDefaultNamespace())), start, end);
	}

	@Override
	public Set<StringTuple> zRevRangeWithScores(String key, long start, long end) {
		return super.zRevRangeWithScores(extractKey(key, extractPrefixStr(getDefaultNamespace())), start, end);
	}

	@Override
	public Set<String> zRevRangeByScore(String key, double min, double max) {
		return super.zRevRangeByScore(extractKey(key, extractPrefixStr(getDefaultNamespace())), min, max);
	}

	@Override
	public Set<StringTuple> zRevRangeByScoreWithScores(String key, double min, double max) {
		return super.zRevRangeByScoreWithScores(extractKey(key, extractPrefixStr(getDefaultNamespace())), min, max);
	}

	@Override
	public Set<String> zRevRangeByScore(String key, double min, double max, long offset, long count) {
		return super.zRevRangeByScore(extractKey(key, extractPrefixStr(getDefaultNamespace())), min, max, offset, count);
	}

	@Override
	public Set<StringTuple> zRevRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
		return super.zRevRangeByScoreWithScores(extractKey(key, extractPrefixStr(getDefaultNamespace())), min, max, offset, count);
	}

	@Override
	public Long zRevRank(String key, String value) {
		return super.zRevRank(extractKey(key, extractPrefixStr(getDefaultNamespace())), value);
	}

	@Override
	public Double zScore(String key, String value) {
		return super.zScore(extractKey(key, extractPrefixStr(getDefaultNamespace())), value);
	}

	@Override
	public Long zUnionStore(String destKey, Aggregate aggregate, int[] weights, String... sets) {
		return super.zUnionStore(extractKey(destKey, extractPrefixStr(getDefaultNamespace())), aggregate, weights, sets);
	}

	@Override
	public Long zUnionStore(String destKey, String... sets) {
		return super.zUnionStore(extractKey(destKey, extractPrefixStr(getDefaultNamespace())), sets);
	}

	@Override
	public Boolean pExpire(String key, long millis) {
		return super.pExpire(extractKey(key, extractPrefixStr(getDefaultNamespace())), millis);
	}

	@Override
	public Boolean pExpireAt(String key, long unixTimeInMillis) {
		return super.pExpireAt(extractKey(key, extractPrefixStr(getDefaultNamespace())), unixTimeInMillis);
	}

	@Override
	public Long pTtl(String key) {
		return super.pTtl(extractKey(key, extractPrefixStr(getDefaultNamespace())));
	}

	@Override
	public <T> T eval(String script, ReturnType returnType, int numKeys, String... keysAndArgs) {
		// TODO Auto-generated method stub
		return super.eval(script, returnType, numKeys, keysAndArgs);
	}

	@Override
	public <T> T evalSha(String scriptSha1, ReturnType returnType, int numKeys, String... keysAndArgs) {
		// TODO Auto-generated method stub
		return super.evalSha(scriptSha1, returnType, numKeys, keysAndArgs);
	}

	@Override
	public void setDeserializePipelineAndTxResults(boolean deserializePipelineAndTxResults) {
		// TODO Auto-generated method stub
		super.setDeserializePipelineAndTxResults(deserializePipelineAndTxResults);
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
		if(getExpireMap() != null && getExpireMap().containsKey(nameSpace)){
			return getExpireMap().get(nameSpace);
		}else{
			return getDefaultExpiration();
		}
	}

	@Override
	public void setObjEx(String nameSpace, String key, Object obj, Long expire) {
		byte[] prefix = extractPrefix(nameSpace);
		byte[] result = extractKey(getStringSerializer().serialize(key), prefix);
		
		if(isSimpleValue(obj)){
			super.setEx(result, expire, getStringSerializer().serialize(String.valueOf(obj)));
		}else{
			super.setEx(result, expire, getValueSerializer().serialize(obj));
		}
	}

	@Override
	public void setObjEx(String key, Object obj, Long expire) {
		this.setObjEx(getDefaultNamespace(), key, obj, expire);
	}

	@Override
	public void setObjEx(String nameSpace, byte[] key, Object obj, Long expire) {
		byte[] prefix = extractPrefix(nameSpace);
		byte[] result = extractKey(key, prefix);
		
		if(isSimpleValue(obj)){
			super.setEx(result, expire, getStringSerializer().serialize(String.valueOf(obj)));
		}else{
			super.setEx(result, expire, getValueSerializer().serialize(obj));
		}
	}

	@Override
	public void setObj(String nameSpace, byte[] key, Object obj) {
		byte[] prefix = extractPrefix(nameSpace);
		byte[] result = extractKey(key, prefix);
		
		if(isSimpleValue(obj)){
			super.set(result, getStringSerializer().serialize(String.valueOf(obj)));
		}else{
			super.set(result, getValueSerializer().serialize(obj));
		}
	}

	@Override
	public void setObj(String nameSpace, String key, Object obj) {
		byte[] prefix = extractPrefix(nameSpace);
		byte[] result = extractKey(getStringSerializer().serialize(key), prefix);
		
		if(isSimpleValue(obj)){
			super.set(result, getStringSerializer().serialize(String.valueOf(obj)));
		}else{
			super.set(result, getValueSerializer().serialize(obj));
		}
	}

	private boolean isSimpleValue(Object obj) {
		return obj instanceof CharSequence || ClassUtils.isPrimitiveOrWrapper(obj.getClass());
	}
	
	private boolean isSimpleType(Class<?> clazz) {
		return CharSequence.class.isAssignableFrom(clazz) || ClassUtils.isPrimitiveOrWrapper(clazz);
	}

	@Override
	public void setObj(String key, Object obj) {
		setObj(getDefaultNamespace(), key, obj);
	}

	@Override
	public void setObj(byte[] key, Object obj) {
		setObj(getDefaultNamespace(), key, obj);
	}

	@Override
	public void put(byte[] key, Object obj) {
		super.set(key, getValueSerializer().serialize(obj));
	}

	@Override
	public void put(String key, Object obj) {
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

	@Override
	public String getStr(String key) {
		byte[] prefix = extractPrefix(getDefaultNamespace());
		byte[] result = extractKey(getStringSerializer().serialize(key), prefix);
		return getStringSerializer().deserialize(super.get(result));
	}

	@Override
	public Boolean getBool(String key) {
		return Boolean.parseBoolean(getStr(key));
	}

	@Override
	public Integer getInt(String key) {
		return Integer.parseInt(getStr(key));
	}

	@Override
	public Long getLong(String key) {
		return Long.parseLong(getStr(key));
	}

	@Override
	public Double getDouble(String key) {
		return Double.parseDouble(getStr(key));
	}

	@Override
	public BigDecimal getDecimal(String key) {
		return new BigDecimal(getStr(key));
	}

	@Override
	public Float getFloat(String key) {
		return Float.parseFloat(getStr(key));
	}

	@Override
	public Short getShort(String key) {
		return Short.parseShort(getStr(key));
	}

	@Override
	public <T> T getObj(byte[] key, Class<T> clazz) {
		Object value = getObj(key);
		if(value == null) return null;
		return extractSimpleTypeConstruct(clazz, value);
	}

	@Override
	public <T> T getObj(String key, Class<T> clazz) {
		Object value = getObj(key);
		if(value == null) return null;
		return extractSimpleTypeConstruct(clazz, value);
	}

	@Override
	public Object getObj(String nameSpace, byte[] key) {
		byte[] prefix = extractPrefix(nameSpace);
		byte[] result = extractKey(key, prefix);
		return getValueSerializer().deserialize(super.get(result));
	}

	private byte[] extractPrefix(String nameSpace) {
		byte[] prefix = new byte[0];
		if(StringUtils.isNotEmpty(nameSpace)){
			prefix = cachePrefix.prefix(nameSpace);
		}
		return prefix;
	}
	
	private String extractPrefixStr(String nameSpace) {
		byte[] prefix = new byte[0];
		if(StringUtils.isNotEmpty(nameSpace)){
			prefix = cachePrefix.prefix(nameSpace);
		}
		return getStringSerializer().deserialize(prefix);
	}

	@Override
	public Object getObj(String nameSpace, String key) {
		byte[] prefix = extractPrefix(nameSpace);
		byte[] result = extractKey(getStringSerializer().serialize(key), prefix);
		return getValueSerializer().deserialize(super.get(result));
	}

	@Override
	public <T> T getObj(String nameSpace, byte[] key, Class<T> clazz) {
		Object value = getObj(nameSpace,key);
		if(value == null) return null;
		return extractSimpleTypeConstruct(clazz, value);
	}

	@Override
	public <T> T getObj(String nameSpace, String key, Class<T> clazz) {
		Object value = getObj(nameSpace,key);
		if(value == null) return null;
		return extractSimpleTypeConstruct(clazz, value);
	}

	@SuppressWarnings("unchecked")
	private <T> T extractSimpleTypeConstruct(Class<T> clazz, Object value) {
		if(isSimpleType(clazz)){
			try {
				return ConstructorUtils.invokeConstructor(clazz, new Object[]{value.toString()}, new Class[]{String.class});
			} catch (Exception e) {
				throw new IllegalStateException(e);
			} 
		}else{
			return (T)value;//JSONParser能够保证类型一致
		}
	}

	@Override
	public List<?> mGetObj(String... keys) {
		List<String> newKeyList = Lists.newArrayList();
		if(keys == null || keys.length == 0) return newKeyList;
		for(String key : keys){
			String prefix = extractPrefixStr(getDefaultNamespace());
			newKeyList.add(extractKey(key, prefix));
		}
		List<String> resultList = super.mGet(newKeyList.toArray(new String[0]));
		List<Object> list = Lists.newArrayList();
		for(String result : resultList){
			list.add(getValueSerializer().deserialize(getStringSerializer().serialize(result)));
		}
		return list;
	}

	private String extractKey(String key, String prefix) {
		return StringUtils.join(prefix,key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> mGetObj(Class<T> clazz, String... keys) {
		List<T> list = Lists.newArrayList();
		if(keys == null || keys.length == 0) return list;
		List<String> newKeyList = Lists.newArrayList();
		for(String key : keys){
			String prefix = extractPrefixStr(getDefaultNamespace());
			newKeyList.add(extractKey(key, prefix));
		}
		List<String> resultList = super.mGet(newKeyList.toArray(new String[0]));
		for(String result : resultList){
			list.add((T)getValueSerializer().deserialize(getStringSerializer().serialize(result)));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void mSetObj(Map<String, Object> map) {
		if(map == null) return ;
		Map<byte[],byte[]> newMap = Maps.newHashMap();
		for(String key : map.keySet()){
			Object obj = map.get(key);
			byte[] prefix = extractPrefix(getDefaultNamespace());
			newMap.put(extractKey(getStringSerializer().serialize(key), prefix), getValueSerializer().serialize(obj));
		}
		if(newMap.size() > 0) super.mSet(newMap);
	}

	@Override
	public void mSetObjEx(Map<String, Object> map) {
		mSetObj(map);
		for(String key : map.keySet()){
			String prefix = extractPrefixStr(getDefaultNamespace());
			super.expire(extractKey(key, prefix), getDefaultExpiration());
		}
	}

	@Override
	public void mSetObjEx(Map<String, Object> map, Long expire) {
		mSetObj(map);
		for(String key : map.keySet()){
			String prefix = extractPrefixStr(getDefaultNamespace());
			super.expire(extractKey(key, prefix), expire);
		}
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
