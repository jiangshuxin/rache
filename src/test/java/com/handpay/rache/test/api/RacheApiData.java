package com.handpay.rache.test.api;

import java.util.Date;

import org.testng.annotations.DataProvider;

import com.handpay.rache.test.anno.service.bean.Student;

public class RacheApiData {

	@DataProvider(name = "testCache")
	public static Object[][] testCache() {
		Student s = new Student();
		s.setAge(12);
		s.setDate(new Date());
		s.setName("sxjiang");
		s.setSex("male");
		return new Object[][] { 
			{ s },
		};

	}

}
