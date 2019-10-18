package com.zhongjian.localservice;

import java.util.List;

public interface OrderService {

	void todoSth();
	
	
	boolean changeDelieverModel(Integer uoid);
	
	
	void distributeOrder(Integer uoid,boolean isFirst,int time);
	
	void lockDistributeOrder(Integer uoid);
	
	void changeAndDistributeOrder(Integer uoid);
	
	
	Integer getDelieverModel(Integer uoid);
	
	
	List<Integer> queryWaitdeliverOrderList();
	
	void checkCVOrder(Integer orderId);
	
}
