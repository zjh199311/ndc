package com.zhongjian.commoncomponent;

import java.util.concurrent.TimeUnit;

import com.zhongjian.util.TaskUtil;

public class TaskBase {

	public void executeTask(Runnable runnable){
		if (TaskUtil.executorTask != null) {
			TaskUtil.executorTask.execute(runnable);
		}
		
	}

	public void executeShedule(Runnable runnable,long initialDelay,long delay){
		if (TaskUtil.executorShedule != null) {
			TaskUtil.executorShedule.scheduleWithFixedDelay(runnable, initialDelay, delay, TimeUnit.SECONDS);
		}
	}
	
	
	
	public void executeDelayShedule(Runnable runnable,long delay){
		if (TaskUtil.executorShedule != null) {
			TaskUtil.executorShedule.schedule(runnable, delay, TimeUnit.SECONDS);
		}
	}
}
