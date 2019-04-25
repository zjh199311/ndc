package com.zhongjian.commoncomponent;

import com.zhongjian.util.TaskUtil;

public class TaskBase {

	public void execute(Runnable runnable){
		TaskUtil.executor.execute(runnable);
	}

}
