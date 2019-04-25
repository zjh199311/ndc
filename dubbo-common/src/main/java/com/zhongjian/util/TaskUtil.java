package com.zhongjian.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskUtil {

	public static ExecutorService executor =  null;
	public static void start(int threadNum) {
		if (executor == null) {
			executor = Executors.newFixedThreadPool(threadNum);
		}
	
	}
}
