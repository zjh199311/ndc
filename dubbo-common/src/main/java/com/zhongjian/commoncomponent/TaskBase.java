package com.zhongjian.commoncomponent;

import java.util.concurrent.TimeUnit;

import com.zhongjian.util.TaskUtil;

public class TaskBase {

	public void executeTask(Runnable runnable){
		TaskUtil.executorTask.execute(runnable);
	}

	public void executeShedule(Runnable runnable,long initialDelay,long delay){
		TaskUtil.executorShedule.scheduleWithFixedDelay(runnable, initialDelay, delay, TimeUnit.SECONDS);
	}
}
