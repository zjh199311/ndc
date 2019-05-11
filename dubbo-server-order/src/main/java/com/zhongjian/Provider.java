package com.zhongjian;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhongjian.util.TaskUtil;

public class Provider {
	public static void main(String[] args) throws Exception {
		
		// 开启异步任务
		TaskUtil.startTask(1);
		// 开启定时任务
		TaskUtil.startShedule(2);
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "META-INF/spring/dubbo-server.xml" });
		context.start();
		Object lock = new Object();
		synchronized (lock) {
			lock.wait();
		}
	}
}
