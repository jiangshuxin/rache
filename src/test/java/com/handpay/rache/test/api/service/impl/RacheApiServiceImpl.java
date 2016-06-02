package com.handpay.rache.test.api.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.handpay.rache.core.spring.StringRedisTemplateX;
import com.handpay.rache.core.spring.connection.StringRedisConnectionX;
import com.handpay.rache.test.anno.service.bean.Student;
import com.handpay.rache.test.api.service.RacheApiService;

@Service("racheApiService")
@DependsOn("RacheBootstrap1")
public class RacheApiServiceImpl implements RacheApiService {
	
	@Autowired
	@Qualifier("stringRedisTemplateX")
	private StringRedisTemplateX stringRedisTemplateX;

	@Override
	public Student queryByName(final String name) {
		Student cache = stringRedisTemplateX.execute(new RedisCallback<Student>() {
			@Override
			public Student doInRedis(RedisConnection connection) throws DataAccessException {
				StringRedisConnectionX conn = (StringRedisConnectionX)connection;
				System.out.println("dubboMonitor="+conn.getObj("dubboMonitor", name));
				System.out.println("racheTest="+conn.getObj("persist", "testKey"));
				System.out.println("simpleValue="+conn.getObj("simple_value_test",StringBuilder.class).getClass().getName());
				return conn.getObj(name,Student.class);
			}
		});
		System.out.println("cache="+cache);
		
		if(cache == null) {
			//visit db
			final Student s = new Student();
			s.setName("db");
			s.setAge(44);
			s.setDate(new Date());
			s.setSex("male");
			
			//save cache object
			stringRedisTemplateX.execute(new RedisCallback<Student>() {
				@Override
				public Student doInRedis(RedisConnection connection) throws DataAccessException {
					StringRedisConnectionX conn = (StringRedisConnectionX)connection;
					conn.setObjEx(name, s,300L);//测试常用方式1
					conn.setObjEx("dubboMonitor", name, s);//测试常用方式2
					conn.setObjEx("test_ns_", name, s, 90L);//测试完整方式
					conn.setObj("simple_value_test", "9000");
					return s;
				}
			});
			return s;
		}
		return cache;
	}

	@Override
	public List queryByName1(final String name) {
		stringRedisTemplateX.keys( "key1");
		stringRedisTemplateX.keys( "key2");
		stringRedisTemplateX.keys( "key3");
		stringRedisTemplateX.keys( "mobile");
		return null;
	}

	@Override
	public List queryByNamePipelined(final String name) {
		
		long begin = System.currentTimeMillis();
		for(int i=0;i<2000;i++){
			stringRedisTemplateX.keys( "key1");
			stringRedisTemplateX.keys( "key2");
			stringRedisTemplateX.keys( "key3");
			stringRedisTemplateX.keys( "mobile");
		}
		long end = System.currentTimeMillis();
		System.out.println("Single thread mode, Nopipelined=="+(end-begin));
		
		begin = System.currentTimeMillis();
		List list = stringRedisTemplateX.executePipelined(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				StringRedisConnectionX conn = (StringRedisConnectionX)connection;
				for(int i=0;i<2000;i++){//超过20，Pipelined性能更优
					conn.keys( "key1");
					conn.keys( "key2");
					conn.keys( "key3");
					conn.keys( "mobile");
				}
				return null;//必须返回null
			}
		});
		end = System.currentTimeMillis();
		System.out.println("Single thread mode, Pipelined=="+(end-begin));
		
		
		begin = System.currentTimeMillis();
		ExecutorService es = Executors.newCachedThreadPool();
		try {
			for(int i=0;i<2000;i++){
				es.submit(new Runnable() {
					@Override
					public void run() {
						stringRedisTemplateX.keys( "key1");
						stringRedisTemplateX.keys( "key2");
						stringRedisTemplateX.keys( "key3");
						stringRedisTemplateX.keys( "mobile");
					}
				}).get();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		end = System.currentTimeMillis();
		System.out.println("Multi thread mode, NoPipelined=="+(end-begin));
		
		
		return list;
	}

	@Override
	public List queryMulti(final Student s) {
		return stringRedisTemplateX.execute(new RedisCallback<List>() {

			@Override
			public List doInRedis(RedisConnection connection) throws DataAccessException {
				StringRedisConnectionX conn = (StringRedisConnectionX)connection;
				Student s2 = s.clone();
				s2.setName(s2.getName()+"03");
				
				Map<String,Object> map = Maps.newHashMap();
				map.put("mget_test01", s);
				map.put("mget_test02", s2);
				conn.mSetObj(map);
				
				return conn.mGetObj(Student.class,"mget_test01","mget_test02");
			}
		});
	}

	@Override
	public List queryByNamePersist(String name) {
		return stringRedisTemplateX.executePipelined(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				StringRedisConnectionX conn = (StringRedisConnectionX)connection;
				Person p = new Person();
				p.setAge(12);
				p.setName("sxjiang");
				conn.setObj("persist", "testKey",p);
				conn.getObj("", "key1");
				conn.getObj("persist", "testKey");
				return null;//必须返回null
			}
		});
	}
	
	public static class Person{
		private String name;
		private int age;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
	}
}
