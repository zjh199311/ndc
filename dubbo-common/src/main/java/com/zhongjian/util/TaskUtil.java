package com.zhongjian.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class TaskUtil {

	public static ExecutorService executorTask = null;

	public static ScheduledExecutorService executorShedule = null;

	public static void startTask(int threadNum) {
		if (executorTask == null) {
			executorTask = Executors.newFixedThreadPool(threadNum);
		}

	}

	public static void startShedule(int threadNum) {
		if (executorShedule == null) {
			executorShedule = Executors.newScheduledThreadPool(threadNum);
		}
	}
}
