package com.handpay.rache.core.spring.connection;

import org.springframework.data.redis.connection.StringRedisConnection;

public interface StringRedisConnectionX extends StringRedisConnection {

	/**
	 * 设置缓存(无命名空间)
	 * @param key
	 * @param value
	 */
	public void setEx(byte[] key, byte[] value) ;

	/**
	 * 设置缓存(无命名空间)
	 * @param key
	 * @param value
	 */
	public void setEx(String key, String value) ;
	
	/**
	 * 设置缓存(无命名空间/只做对象序列化)
	 * @param key
	 * @param obj
	 */
	public void setObj(byte[] key,Object obj);
	/**
	 * 设置缓存(无命名空间/只做对象序列化)
	 * @param key
	 * @param obj
	 */
	public void setObj(String key,Object obj);
	
	/**
	 * 使用系统默认配置进行缓存。默认配置包括：
	 * <li>缓存过期时间
	 * <li>缓存key命名空间(即前缀)
	 * @param key
	 * @param obj
	 */
	public void setObjEx(byte[] key,Object obj);
	/**
	 * 使用系统默认配置进行缓存。默认配置包括：
	 * <li>缓存过期时间
	 * <li>缓存key命名空间(即前缀)
	 * @param key
	 * @param obj
	 */
	public void setObjEx(String key,Object obj);
	
	/**
	 * 设置缓存(使用nameSpace匹配的过期时间，如不匹配则使用默认过期时间)
	 * @param nameSpace
	 * @param key
	 * @param obj
	 */
	public void setObjEx(String nameSpace,String key,Object obj);
	
	/**
	 * 设置缓存(使用nameSpace匹配的过期时间，如不匹配则使用默认过期时间)
	 * @param nameSpace
	 * @param key
	 * @param obj
	 */
	public void setObjEx(String nameSpace,byte[] key,Object obj);
	
	/**
	 * 设置缓存(最完整接口之一)
	 * @param nameSpace
	 * @param key
	 * @param obj
	 * @param expire
	 */
	public void setObjEx(String nameSpace,String key,Object obj,Long expire);
	/**
	 * 设置缓存(最完整接口之一)
	 * @param nameSpace
	 * @param key
	 * @param obj
	 * @param expire
	 */
	public void setObjEx(String nameSpace,byte[] key,Object obj,Long expire);
	
	public Object getObj(byte[] key); 
	public Object getObj(String key);
	public Object getObj(String nameSpace,byte[] key); 
	public Object getObj(String nameSpace,String key);
	
	public <T> T getObj(byte[] key,Class<T> clazz);
	public <T> T getObj(String key,Class<T> clazz);
	public <T> T getObj(String nameSpace,byte[] key,Class<T> clazz);
	public <T> T getObj(String nameSpace,String key,Class<T> clazz);
}
