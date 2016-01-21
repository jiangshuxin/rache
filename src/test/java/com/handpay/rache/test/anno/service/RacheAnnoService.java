package com.handpay.rache.test.anno.service;

import java.util.Date;

import com.handpay.rache.test.anno.service.bean.Student;

public interface RacheAnnoService {

	Student queryByName(String name);
	void update(Student s);
	Student queryByMany(String name,int age,Date date);
}
