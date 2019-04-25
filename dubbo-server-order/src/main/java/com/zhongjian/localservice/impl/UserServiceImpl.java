package com.zhongjian.localservice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.dao.jdbctemplate.UserDao;
import com.zhongjian.localservice.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	
	@Override
	public void updateUserMarketIdById(Integer marketId,Integer uid) {
		userDao.updateLateMarket(marketId, uid);
		
	}

 
}
