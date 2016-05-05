package com.handpay.rache.test.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.handpay.rache.test.anno.service.bean.Student;
import com.handpay.rache.test.api.service.RacheApiService;

@Test(dataProviderClass = RacheApiData.class)
@ContextConfiguration(locations = { "classpath:spring/spring-application.xml" })
public class RacheApiTest extends AbstractTestNGSpringContextTests {
	@Autowired
	private RacheApiService racheApiService;

	@Test(dataProvider = "testCache")
	public void testCache(Student s){
		Student student = racheApiService.queryByName(s.getName());
		System.out.println(student);
	}
	
	@Test(dataProvider = "testCache")
	public void testMaxConn(final Student s) throws InterruptedException{
		for(int i=0;i<10000;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					for(int j=0;j<5;j++){
						List list = racheApiService.queryByName1(s.getName()+j);
						//System.out.println(list);
					}
				}
			}).start();
		}
		
		Thread.sleep(100000);
	}
}
