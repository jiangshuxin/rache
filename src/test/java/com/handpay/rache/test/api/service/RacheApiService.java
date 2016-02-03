package com.handpay.rache.test.api.service;

import java.util.List;

import com.handpay.rache.test.anno.service.bean.Student;

public interface RacheApiService {

	Student queryByName(String name);
	List queryByName1(String name);
}
