package com.zhongjian;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Provider {
	  public static void main(String[] args) throws Exception {
	        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/dubbo-server.xml"});
	        context.start();
	        Object lock = new Object(); 
	        synchronized(lock){
	        	lock.wait();
	        }
	    }
}
