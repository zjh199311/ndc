package com.zhongjian.shedule;

import java.util.Iterator;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.zhongjian.commoncomponent.TaskBase;
import com.zhongjian.localservice.OrderService;

@Component
public class OrderShedule extends TaskBase implements ApplicationListener<ContextRefreshedEvent> {

@Autowired
private OrderService orderService;

	void orderShedule() {
		// 定时执行任务，每隔5分钟钟执行一次
		executeShedule(new Runnable() {
			@Override
			public void run() {
				orderService.todoSth();
			}
		}, 0, 300);
	}

	//延时处理订单
	public void delayHandleCVOrder(Integer orderId,Integer time) {
		if (orderService.getDelieverModel(orderId) == 2) {
			orderService.lockDistributeOrder(orderId);
			return;
		}
		// 延时60s把订单改为平台处理
		executeDelayShedule(new Runnable() {
			@Override
			public void run() {
				orderService.changeAndDistributeOrder(orderId);
			}
		},  time);
	}
	

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.orderShedule();
//		List<Integer> orderList = orderService.queryWaitdeliverOrderList();
//		for (Iterator iterator = orderList.iterator(); iterator.hasNext();) {
//			Integer orderId = (Integer) iterator.next();
//			delayHandleCVOrder(orderId, 60);
//		}
		
	}
	
	
	//检查订单是否有金额错误
	public void checkCVOrder(Integer orderId) {
		// 延时60s把订单改为平台处理
		executeDelayShedule(new Runnable() {
			@Override
			public void run() {
				orderService.checkCVOrder(orderId);
			}
		},  5);
	}
}
