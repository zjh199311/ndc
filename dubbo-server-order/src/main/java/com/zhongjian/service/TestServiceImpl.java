package com.zhongjian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("testService")
public class TestServiceImpl implements TestService {

	
	public String testMethod(String param) {
		return param + " Hello World!one";
	}

	

}