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
	public void testCache1(Student s){
		List list = racheApiService.queryByName1(s.getName());
		System.out.println(list);
	}
}
