package com.zhongjian.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhongjian.commoncomponent.TaskBase;
import com.zhongjian.localservice.MessageService;


@Component
public class OrderTask extends TaskBase {

	@Autowired
	MessageService messageService;
	
	public void handleOrderPush(Integer orderId) {
		executeTask(new Runnable() {
			@Override
			public void run() {
				messageService.messagePush(orderId);
			}
		});
	}
}
