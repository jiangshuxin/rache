package com.handpay.rache.test.api;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import com.handpay.rache.test.anno.service.bean.Student;
import com.handpay.rache.test.api.service.RacheApiService;

import redis.clients.jedis.JedisPoolConfig;

@Test(dataProviderClass = RacheApiData.class)
@ContextConfiguration(locations = { "classpath:spring/spring-application.xml" })
public class RacheApiTest extends AbstractTestNGSpringContextTests {
	@Autowired
	private RacheApiService racheApiService;
	
	@Autowired
	@Qualifier("jedisPoolConfig")
	private JedisPoolConfig JedisPoolConfig;

	@Test(dataProvider = "testCache")
	public void testCache(Student s){
		Student student = racheApiService.queryByName(s.getName());
		System.out.println(student.getClass());
		/*System.out.println(JedisPoolConfig.getMaxActive());*/
	}
	
	@Test(dataProvider = "testCache")
	public void testPipeline(Student s){
		List list = racheApiService.queryByNamePipelined(s.getName());
		System.out.println(list);
	}
	
	@Test(dataProvider = "testCache")
	public void testPersist(Student s){
		List list = racheApiService.queryByNamePersist(s.getName());
		System.out.println(list);
		System.out.println(list.get(1).getClass());
	}
	
	@Test(dataProvider = "testCache")
	public void testMGet(Student s){
		List list = racheApiService.queryMulti(s);
		System.out.println(list);
	}
	
	@Test(dataProvider = "testCache")
	public void testMaxConn(final Student s) throws InterruptedException, ExecutionException{
		//maxIdle/maxActive=100->804   1000->1300  1->4744  10->1833  有一项为1就4800
		long begin = System.currentTimeMillis();
		ExecutorService es = Executors.newCachedThreadPool();
		List<Future<?>> list = Lists.newArrayList();
		try {
			for(int i=0;i<20000;i++){
				list.add(es.submit(new Runnable() {
					@Override
					public void run() {
						racheApiService.queryByName1(s.getName());
					}
				}));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		for(Future<?> f :list){
			f.get();
		}
		long end = System.currentTimeMillis();
		System.out.println("Multi thread mode, NoPipelined=="+(end-begin));
	}
}
