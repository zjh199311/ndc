package com.zhongjian;

import com.zhongjian.util.LogUtil;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Provider {
	  public static void main(String[] args) throws Exception {
	        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/dubbo-server.xml"});
	        context.start();
	        if(context.isRunning()){
				LogUtil.info("启动成功","");
			}
	        Object lock = new Object(); 
	        synchronized(lock){
	        	lock.wait();
	        }
	    }
}
