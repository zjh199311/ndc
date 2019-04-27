package com.zhongjian;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhongjian.util.TaskUtil;

public class Provider {
	  public static void main(String[] args) throws Exception {
	        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/dubbo-server.xml"});
	        context.start();
	        //开启异步任务
	        TaskUtil.start(1);
	        Object lock = new Object(); 
	        synchronized(lock){
	        	lock.wait();
	        }
	    }
}
