package com.zhongjian.shedule;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhongjian.commoncomponent.TaskBase;
import com.zhongjian.localservice.OrderService;

@Component
public class OrderShedule extends TaskBase {

@Autowired
private OrderService orderService;

	@PostConstruct
	void startAll() {
		this.orderShedule();
	}
	void orderShedule() {
		// 定时执行任务，每隔2秒钟执行一次
		executeShedule(new Runnable() {
			@Override
			public void run() {
				orderService.todoSth();
			}
		}, 0, 300);
	}

}
