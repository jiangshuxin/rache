package com.handpay.rache.core.spring.bootstrap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handpay.rache.core.spring.RedisCacheManagerX;
import com.handpay.rache.core.spring.StringRedisTemplateX;
import com.handpay.rache.core.spring.serializer.FastJSONRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * rache引导类。能够完成StringRedisTemplateX系列类的装载，并定义默认的bean命名方式，是屏蔽复杂Bean装载过程的轻量级解决方案（但请注意依赖关系）
 * @see com.handpay.rache.core.spring.StringRedisTemplateX
 * @author sxjiang
 *
 */
public class RacheBootstrap implements ApplicationContextAware,InitializingBean,BeanPostProcessor{
	//StringRedisTemplateX的beanId
	private String targetBeanId;
	//命名空间/超时时间映射beanId
	private String expireMapBeanId;
	//属性字典
	private Map<String,String> propMap;
	//redis connection连接超时时间
	private int redisTimeout;
	//redis连接密码
	private String password;
	//默认过期时间
	private long defaultExpiration;
	//默认命名空间
	private String defaultNamespace;
	//缓存服务监视器url，用于获取redis连接地址(host/port)
	private String cacheServerURL;
	private ApplicationContext applicationContext;
	
	private static int count = 0;
	//各个组件默认bean的注册id
	private String jedisBeanId = "jedisPoolConfig";
	private String jedisFacBeanId = "jedisConnectionFactory";
	private String fastBeanId = "fastJSONRedisSerializer";
	private String redisTempBeanId = "redisTemplate";
	private String strRedisTempBeanId = "stringRedisTemplate";
	private String cacheBeanId = "cacheManager";
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	private static synchronized String getSuffix(){
		if(count <= 0){
			count++;
			return StringUtils.EMPTY;
		}else{
			count++;
			return String.valueOf(count);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String suffix = getSuffix();
		String jedisBeanName = StringUtils.join(getJedisBeanId(),suffix);
		String jedisFacBeanName = StringUtils.join(getJedisFacBeanId(),suffix);
		String fastBeanName = getFastBeanId();
		String redisTempBeanName = StringUtils.join(getRedisTempBeanId(),suffix);
		String strRedisTempBeanName = StringUtils.join(getStrRedisTempBeanId(),suffix);
		String cacheBeanName = StringUtils.join(getCacheBeanId(),suffix);
		
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
		
		extractJedisPoolConfigBuilder(jedisBeanName, defaultListableBeanFactory);
		
		extractJedisConnectionFactoryBuilder(jedisBeanName, jedisFacBeanName, defaultListableBeanFactory);
		
		extractFastJSONRedisSerializerBuilder(fastBeanName, defaultListableBeanFactory);
		
		extractRedisTemplateBuilder(jedisFacBeanName, fastBeanName, redisTempBeanName, defaultListableBeanFactory);
		
		extractStringRedisTemplateBuilder(redisTempBeanName, strRedisTempBeanName, defaultListableBeanFactory);
		
		extractStringRedisTemplateXBuilder(strRedisTempBeanName, defaultListableBeanFactory);
		
		extractRedisCacheManagerXBuilder(cacheBeanName, defaultListableBeanFactory);

		afterBeanCreated();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void afterBeanCreated() {
		RedisTemplate template = applicationContext.getBean(getRedisTempBeanId(), RedisTemplate.class);
		template.setKeySerializer(template.getStringSerializer());
		template.setHashKeySerializer(template.getStringSerializer());
	}

	private void extractRedisCacheManagerXBuilder(String cacheBeanName,
			DefaultListableBeanFactory defaultListableBeanFactory) {
		BeanDefinitionBuilder redisCacheManagerXBuilder = BeanDefinitionBuilder.genericBeanDefinition(RedisCacheManagerX.class);
		redisCacheManagerXBuilder.addConstructorArgReference("redisTemplate");
		redisCacheManagerXBuilder.addPropertyValue("usePrefix", true);
		redisCacheManagerXBuilder.addPropertyValue("defaultExpiration", getDefaultExpiration());
		if(StringUtils.isNotEmpty(getExpireMapBeanId())) {
			redisCacheManagerXBuilder.addDependsOn(getExpireMapBeanId());
			redisCacheManagerXBuilder.addPropertyReference("expires", getExpireMapBeanId());
		}
		defaultListableBeanFactory.registerBeanDefinition(cacheBeanName, redisCacheManagerXBuilder.getBeanDefinition());
	}

	private void extractStringRedisTemplateXBuilder(String strRedisTempBeanName,
			DefaultListableBeanFactory defaultListableBeanFactory) {
		BeanDefinitionBuilder stringRedisTemplateXBuilder = BeanDefinitionBuilder.genericBeanDefinition(StringRedisTemplateX.class);
		stringRedisTemplateXBuilder.setParentName(strRedisTempBeanName);
		stringRedisTemplateXBuilder.addPropertyValue("defaultExpiration", getDefaultExpiration());
		stringRedisTemplateXBuilder.addPropertyValue("defaultNamespace", getDefaultNamespace());
		if(StringUtils.isNotEmpty(getExpireMapBeanId())) {
			stringRedisTemplateXBuilder.addDependsOn(getExpireMapBeanId());
			stringRedisTemplateXBuilder.addPropertyReference("expireMap", getExpireMapBeanId());
		}
		defaultListableBeanFactory.registerBeanDefinition(getTargetBeanId(), stringRedisTemplateXBuilder.getBeanDefinition());
	}

	private void extractStringRedisTemplateBuilder(String redisTempBeanName, String strRedisTempBeanName,
			DefaultListableBeanFactory defaultListableBeanFactory) {
		BeanDefinitionBuilder stringRedisTemplateBuilder = BeanDefinitionBuilder.genericBeanDefinition(StringRedisTemplate.class);
		stringRedisTemplateBuilder.setParentName(redisTempBeanName);
		defaultListableBeanFactory.registerBeanDefinition(strRedisTempBeanName, stringRedisTemplateBuilder.getBeanDefinition());
	}

	private void extractRedisTemplateBuilder(String jedisFacBeanName, String fastBeanName, String redisTempBeanName,
			DefaultListableBeanFactory defaultListableBeanFactory) {
		BeanDefinitionBuilder redisTemplateBuilder = BeanDefinitionBuilder.genericBeanDefinition(RedisTemplate.class);
		redisTemplateBuilder.addPropertyReference("connectionFactory", jedisFacBeanName);
		redisTemplateBuilder.addPropertyReference("defaultSerializer", fastBeanName);
		defaultListableBeanFactory.registerBeanDefinition(redisTempBeanName, redisTemplateBuilder.getBeanDefinition());
	}

	private void extractFastJSONRedisSerializerBuilder(String fastBeanName,
			DefaultListableBeanFactory defaultListableBeanFactory) {
		if(!applicationContext.containsBean(fastBeanName)){
			BeanDefinitionBuilder fastJSONRedisSerializerBuilder = BeanDefinitionBuilder.genericBeanDefinition(FastJSONRedisSerializer.class);
			defaultListableBeanFactory.registerBeanDefinition(fastBeanName, fastJSONRedisSerializerBuilder.getBeanDefinition());
		}
	}

	private void extractJedisConnectionFactoryBuilder(String jedisBeanName, String jedisFacBeanName,
			DefaultListableBeanFactory defaultListableBeanFactory) throws IOException {
		BeanDefinitionBuilder jedisConnectionFactoryBuilder = BeanDefinitionBuilder.genericBeanDefinition(JedisConnectionFactory.class);
		if(redisTimeout > 0) jedisConnectionFactoryBuilder.addPropertyValue("timeout", redisTimeout);
		if(StringUtils.isNotEmpty(getPassword())) jedisConnectionFactoryBuilder.addPropertyValue("password", getPassword());
		jedisConnectionFactoryBuilder.addPropertyReference("poolConfig", jedisBeanName);
		extractHostAndPort(jedisConnectionFactoryBuilder);
		defaultListableBeanFactory.registerBeanDefinition(jedisFacBeanName, jedisConnectionFactoryBuilder.getBeanDefinition());
	}

	private void extractJedisPoolConfigBuilder(String jedisBeanName,
			DefaultListableBeanFactory defaultListableBeanFactory) {
		BeanDefinitionBuilder jedisPoolConfigBuilder = BeanDefinitionBuilder.genericBeanDefinition(JedisPoolConfig.class);
		for(String key : getPropMap().keySet()){
			jedisPoolConfigBuilder.addPropertyValue(key, getPropMap().get(key));
		}
		defaultListableBeanFactory.registerBeanDefinition(jedisBeanName, jedisPoolConfigBuilder.getBeanDefinition());
	}

	private void extractHostAndPort(BeanDefinitionBuilder jedisConnectionFactoryBuilder) throws IOException {
		String in;
		URL server_url = null;
		try {
			server_url = new URL(StringUtils.join(getCacheServerURL(),"?appCode=",getDefaultNamespace(),"&nodeCode=test"));
			URLConnection urlCon = server_url.openConnection();
			urlCon.setConnectTimeout(2000);
			urlCon.setReadTimeout(2000);
			InputStream input = urlCon.getInputStream();
			in = IOUtils.toString(input);
			input.close();
		} catch (Exception e) {
			throw new IllegalStateException(String.format("connect error! please check cacheServerURL=%s", getCacheServerURL()),e);
		}
		JSONObject jsonObj = (JSONObject)JSON.parse(in);
		String message = jsonObj.getString("message");
		if(!StringUtils.equals("SUCCESS", message)) throw new IllegalStateException(String.format("cacheServerURL=%s returns %s", getCacheServerURL()+"?"+server_url.getQuery(),message));
		//String appCode = jsonObj.getString("appCode");
		JSONArray array = jsonObj.getJSONArray("address");
		if(array != null && array.size() > 0){
			JSONObject address = array.getJSONObject(0);
			jedisConnectionFactoryBuilder.addPropertyValue("hostName", address.getString("host"));
			jedisConnectionFactoryBuilder.addPropertyValue("port", address.getString("port"));
		}
		
	}

	public String getTargetBeanId() {
		return targetBeanId;
	}

	public void setTargetBeanId(String targetBeanId) {
		this.targetBeanId = targetBeanId;
	}

	public Map<String, String> getPropMap() {
		return propMap;
	}

	public void setPropMap(Map<String, String> propMap) {
		this.propMap = propMap;
	}

	public int getRedisTimeout() {
		return redisTimeout;
	}

	public void setRedisTimeout(int redisTimeout) {
		this.redisTimeout = redisTimeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getDefaultExpiration() {
		return defaultExpiration;
	}

	public void setDefaultExpiration(long defaultExpiration) {
		this.defaultExpiration = defaultExpiration;
	}

	public String getDefaultNamespace() {
		return defaultNamespace;
	}

	public void setDefaultNamespace(String defaultNamespace) {
		this.defaultNamespace = defaultNamespace;
	}

	public String getCacheServerURL() {
		return cacheServerURL;
	}

	public void setCacheServerURL(String cacheServerURL) {
		this.cacheServerURL = cacheServerURL;
	}

	public String getJedisBeanId() {
		return jedisBeanId;
	}

	public void setJedisBeanId(String jedisBeanName) {
		this.jedisBeanId = jedisBeanName;
	}

	public String getJedisFacBeanId() {
		return jedisFacBeanId;
	}

	public void setJedisFacBeanId(String jedisFacBeanName) {
		this.jedisFacBeanId = jedisFacBeanName;
	}

	public String getFastBeanId() {
		return fastBeanId;
	}

	public void setFastBeanId(String fastBeanName) {
		this.fastBeanId = fastBeanName;
	}

	public String getRedisTempBeanId() {
		return redisTempBeanId;
	}

	public void setRedisTempBeanId(String redisTempBeanName) {
		this.redisTempBeanId = redisTempBeanName;
	}

	public String getStrRedisTempBeanId() {
		return strRedisTempBeanId;
	}

	public void setStrRedisTempBeanId(String strRedisTempBeanName) {
		this.strRedisTempBeanId = strRedisTempBeanName;
	}

	public String getCacheBeanId() {
		return cacheBeanId;
	}

	public void setCacheBeanId(String cacheBeanName) {
		this.cacheBeanId = cacheBeanName;
	}

	public String getExpireMapBeanId() {
		return expireMapBeanId;
	}

	public void setExpireMapBeanId(String expireMapBeanId) {
		this.expireMapBeanId = expireMapBeanId;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
