package com.zhongjian.localservice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.dao.jdbctemplate.AddressDao;
import com.zhongjian.localservice.addressService;

@Service
public class adressServiceImpl implements addressService{

	@Autowired
	AddressDao addressDao;
	
	public void updateDefaultAdress(Integer addressId,Integer uid) {
		addressDao.updateDefaultAdress(addressId, uid);
	}

}
