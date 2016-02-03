package com.handpay.rache.test.api.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import com.handpay.rache.core.spring.StringRedisTemplateX;
import com.handpay.rache.core.spring.connection.StringRedisConnectionX;
import com.handpay.rache.test.anno.service.bean.Student;
import com.handpay.rache.test.api.service.RacheApiService;

@Service("racheApiService")
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
				System.out.println("racheTest="+conn.getObj("racheTest", name));
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
					conn.setObjEx(name, s);//测试常用方式1
					conn.setObjEx("dubboMonitor", name, s);//测试常用方式2
					conn.setObjEx("test_ns_", name, s, 90L);//测试完整方式
					return s;
				}
			});
			return s;
		}
		return cache;
	}

	@Override
	public List queryByName1(final String name) {
		return stringRedisTemplateX.execute(new SessionCallback<List>() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public List execute(RedisOperations operations) throws DataAccessException {
				operations.multi();
				operations.boundValueOps("dubboMonitor"+name).get();
				operations.boundHashOps("hkey1").put(name, "test");
				operations.boundHashOps("hkey1").get(name);
				return operations.exec();
			}
		});
	}
}
