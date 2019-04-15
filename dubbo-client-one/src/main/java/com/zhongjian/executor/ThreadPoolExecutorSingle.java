package com.zhongjian.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExecutorSingle {
	
	public static ExecutorService executor =  Executors.newCachedThreadPool();
}
