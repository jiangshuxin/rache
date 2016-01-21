package com.handpay.rache.test.anno.service.impl;

import java.util.Date;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.handpay.rache.test.anno.service.RacheAnnoService;
import com.handpay.rache.test.anno.service.bean.Student;

@Service("dubboMonitorService")
public class RacheAnnoServiceImpl implements RacheAnnoService {
	
	@Override
	@Cacheable(value = "racheTest")
	public Student queryByName(String name) {
		Student s = new Student();
		s.setAge(22);
		s.setDate(new Date());
		s.setName("cached name");
		s.setSex("female");
		System.out.println("actual method exec!!!");
		return s;
	}

	@Override
	@CacheEvict(value="dubboMonitor",key="#student.getName()")
	public void update(Student student) {
		System.out.println("actual method exec!!!");
	}

	@Override
	@Cacheable(value = "dubboMonitor",key="#name",condition="#name.length() > 6")
	public Student queryByMany(String name, int age, Date date) {
		Student s = new Student();
		s.setAge(age);
		s.setDate(date);
		s.setName(name);
		s.setSex("female");
		System.out.println("actual method exec!!!");
		return s;
	}
}
