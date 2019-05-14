package com.zhongjian.task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhongjian.commoncomponent.TaskBase;
import com.zhongjian.localservice.UserService;
import com.zhongjian.localservice.addressService;

@Component
public class AddressTask extends TaskBase{

	@Autowired
	private addressService addressService;

	@Autowired
	private UserService userService;
	

	
	public void setAddressTask(Integer id,Integer uid) {
		executeTask(new Runnable() {
			@Override
			public void run() {
				System.out.println("设置" + id + "针对用户" + uid);
				addressService.updateDefaultAdress(id, uid);
			}
		});
	}
	
	public void setLateMarket(Integer marketId,Integer uid) {
		executeTask(new Runnable() {
			@Override
			public void run() {
				userService.updateUserMarketIdById(marketId, uid);
			}
		});
	}


	
}
