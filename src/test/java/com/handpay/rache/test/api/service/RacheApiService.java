package com.handpay.rache.test.api.service;

import com.handpay.rache.test.anno.service.bean.Student;

public interface RacheApiService {

	Student queryByName(String name);
}
