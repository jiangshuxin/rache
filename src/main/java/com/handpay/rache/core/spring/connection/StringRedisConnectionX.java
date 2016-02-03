package com.handpay.rache.core.spring.connection;

import org.springframework.data.redis.connection.StringRedisConnection;

public interface StringRedisConnectionX extends StringRedisConnection {

	/**
	 * ���û���(�������ռ�)
	 * @param key
	 * @param value
	 */
	public void setEx(byte[] key, byte[] value) ;

	/**
	 * ���û���(�������ռ�)
	 * @param key
	 * @param value
	 */
	public void setEx(String key, String value) ;
	
	/**
	 * ���û���(�������ռ�/ֻ���������л�)
	 * @param key
	 * @param obj
	 */
	public void setObjOriginal(byte[] key,Object obj);
	/**
	 * ���û���(�������ռ�/ֻ���������л�)
	 * @param key
	 * @param obj
	 */
	public void setObjOriginal(String key,Object obj);
	
	/**
	 * ʹ��ϵͳĬ�����ý��л��档Ĭ�����ð�����
	 * <li>�������ʱ��
	 * <li>����key�����ռ�(��ǰ׺)
	 * @param key
	 * @param obj
	 */
	public void setObjEx(byte[] key,Object obj);
	/**
	 * ʹ��ϵͳĬ�����ý��л��档Ĭ�����ð�����
	 * <li>�������ʱ��
	 * <li>����key�����ռ�(��ǰ׺)
	 * @param key
	 * @param obj
	 */
	public void setObjEx(String key,Object obj);
	
	/**
	 * ���û���(ʹ��nameSpaceƥ��Ĺ���ʱ�䣬�粻ƥ����ʹ��Ĭ�Ϲ���ʱ��)
	 * @param nameSpace
	 * @param key
	 * @param obj
	 */
	public void setObjEx(String nameSpace,String key,Object obj);
	
	/**
	 * ���û���(ʹ��nameSpaceƥ��Ĺ���ʱ�䣬�粻ƥ����ʹ��Ĭ�Ϲ���ʱ��)
	 * @param nameSpace
	 * @param key
	 * @param obj
	 */
	public void setObjEx(String nameSpace,byte[] key,Object obj);
	
	/**
	 * ���û���(�������ӿ�֮һ)
	 * @param nameSpace
	 * @param key
	 * @param obj
	 * @param expire
	 */
	public void setObjEx(String nameSpace,String key,Object obj,Long expire);
	/**
	 * ���û���(�������ӿ�֮һ)
	 * @param nameSpace
	 * @param key
	 * @param obj
	 * @param expire
	 */
	public void setObjEx(String nameSpace,byte[] key,Object obj,Long expire);
	
	/**
	 * ��ȡ�������(Ĭ�������ռ�)
	 * @param key
	 * @return
	 */
	public Object getObj(byte[] key);
	/**
	 * ��ȡ�������(Ĭ�������ռ�)
	 * @param key
	 * @return
	 */
	public Object getObj(String key);
	/**
	 * ��ȡ�������
	 * @param nameSpace
	 * @param key
	 * @return
	 */
	public Object getObj(String nameSpace,byte[] key); 
	/**
	 * ��ȡ�������
	 * @param nameSpace
	 * @param key
	 * @return
	 */
	public Object getObj(String nameSpace,String key);
	
	/**
	 * ��ȡ�������(Ĭ�������ռ�)
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T getObj(byte[] key,Class<T> clazz);
	/**
	 * ��ȡ�������(Ĭ�������ռ�)
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T getObj(String key,Class<T> clazz);
	/**
	 * ��ȡ�������
	 * @param nameSpace
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T getObj(String nameSpace,byte[] key,Class<T> clazz);
	/**
	 * ��ȡ�������
	 * @param nameSpace
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T getObj(String nameSpace,String key,Class<T> clazz);
}
