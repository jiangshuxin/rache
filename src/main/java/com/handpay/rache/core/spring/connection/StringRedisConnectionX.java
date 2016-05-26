package com.handpay.rache.core.spring.connection;

import java.util.List;
import java.util.Map;

import org.springframework.data.redis.connection.StringRedisConnection;

/**
 * 实用型扩展接口StringRedisConnectionX
 * <li>方法参数体现命名空间/超时时间
 * <li>更人性化的类型选择
 * @author sxjiang
 *
 */
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
	 * 设置持久对象(指定命名空间/无过期时间)
	 * @param nameSpace
	 * @param key
	 * @param obj
	 */
	public void setObj(String nameSpace,byte[] key,Object obj);
	/**
	 * 设置持久对象(指定命名空间/无过期时间)
	 * @param nameSpace
	 * @param key
	 * @param obj
	 */
	public void setObj(String nameSpace,String key,Object obj);
	
	/**
	 * 设置持久对象(默认命名空间/无过期时间)
	 * @param key
	 * @param obj
	 */
	public void setObj(String key,Object obj);
	
	/**
	 * 设置持久对象(默认命名空间/无过期时间)
	 * @param key
	 * @param obj
	 */
	public void setObj(byte[] key,Object obj);
	
	/**
	 * 设置持久对象(无命名空间/无过期时间)
	 * @param key
	 * @param obj
	 */
	public void put(byte[] key,Object obj);
	
	/**
	 * 设置持久对象(无命名空间/无过期时间)
	 * @param key
	 * @param obj
	 */
	public void put(String key,Object obj);
	
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
	 * 设置缓存(使用默认命名空间，指定过期时间)
	 * @param nameSpace
	 * @param key
	 * @param obj
	 */
	public void setObjEx(String key,Object obj,Long expire);
	
	/**
	 * 设置缓存(最完整接口之一)
	 * @param nameSpace
	 * @param key
	 * @param obj
	 * @param expire
	 */
	public void setObjEx(String nameSpace,byte[] key,Object obj,Long expire);
	
	/**
	 * 获取缓存对象(默认命名空间)
	 * @param key
	 * @return
	 */
	public Object getObj(byte[] key);
	/**
	 * 获取缓存对象(默认命名空间)
	 * @param key
	 * @return
	 */
	public Object getObj(String key);
	/**
	 * 获取缓存对象
	 * @param nameSpace
	 * @param key
	 * @return
	 */
	public Object getObj(String nameSpace,byte[] key); 
	/**
	 * 获取缓存对象
	 * @param nameSpace
	 * @param key
	 * @return
	 */
	public Object getObj(String nameSpace,String key);
	
	/**
	 * 获取缓存对象(默认命名空间)
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T getObj(byte[] key,Class<T> clazz);
	/**
	 * 获取缓存对象(默认命名空间)
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T getObj(String key,Class<T> clazz);
	/**
	 * 获取缓存对象
	 * @param nameSpace
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T getObj(String nameSpace,byte[] key,Class<T> clazz);
	/**
	 * 获取缓存对象
	 * @param nameSpace
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T getObj(String nameSpace,String key,Class<T> clazz);
	
	/**
	 * 获取多个对象(默认命名空间)
	 * @param keys
	 * @return
	 */
	public List<?> mGetObj(String... keys);
	
	/**
	 * 获取多个对象(默认命名空间)
	 * @param clazz
	 * @param keys
	 * @return
	 */
	public <T> List<T> mGetObj(Class<T> clazz,String... keys);
	
	/**
	 * 设置多个对象(默认命名空间)
	 * @param map
	 */
	public void mSetObj(Map<String,Object> map);
}
