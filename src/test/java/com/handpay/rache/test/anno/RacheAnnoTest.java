package com.handpay.rache.test.anno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.handpay.rache.test.anno.service.RacheAnnoService;
import com.handpay.rache.test.anno.service.bean.Student;

@Test(dataProviderClass = RacheAnnoData.class)
@ContextConfiguration(locations = { "classpath:spring/spring-application.xml" })
public class RacheAnnoTest extends AbstractTestNGSpringContextTests {
	@Autowired
	private RacheAnnoService racheAnnoService;

	@Test(dataProvider = "testCache")
	public void testCache(Student s){
		Student student = racheAnnoService.queryByName(s.getName());
		System.out.println("racheTest="+student);
	}
	
	//@Test(dataProvider = "testCache")
	public void testCache2(Student s){
		Student student = racheAnnoService.queryByMany(s.getName(),s.getAge(),s.getDate());
		System.out.println(student);
	}
	
	@Test(dataProvider = "testCache")
	public void testCache3(Student s){
		racheAnnoService.update(s);
		Student student = racheAnnoService.queryByMany(s.getName(),s.getAge(),s.getDate());
		System.out.println("dubboMonitor="+student);
	}
}
