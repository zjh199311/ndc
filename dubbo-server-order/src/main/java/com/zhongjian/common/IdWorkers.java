package com.zhongjian.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhongjian.commoncomponent.PropUtil;
import com.zhongjian.util.IdWorkerUtil;

@Component
public class IdWorkers {

	private IdWorkerUtil riderOrderIdWork = null;

	private IdWorkerUtil orderIdWork = null;

	private IdWorkerUtil outTradeIdWork = null;

	private IdWorkerUtil cvOrderIdWork = null;

	private IdWorkerUtil cvUserOrderIdWork = null;
	
	private IdWorkerUtil cvOutTradeIdWork = null;
	

	@Autowired
	private PropUtil propUtil;

	public IdWorkerUtil getRiderOrderIdWork() {
		if (riderOrderIdWork == null) {
			synchronized (this) {
				if (riderOrderIdWork == null) {
					riderOrderIdWork = new IdWorkerUtil(Integer.valueOf(propUtil.getWorkerId()),
							Integer.valueOf(propUtil.getDatacenterId()), 0L);
				}
			}
		}
		return riderOrderIdWork;
	}

	public IdWorkerUtil getOrderIdWork() {
		if (orderIdWork == null) {
			synchronized (this) {
				if (orderIdWork == null) {
					orderIdWork = new IdWorkerUtil(Integer.valueOf(propUtil.getWorkerId()),
							Integer.valueOf(propUtil.getDatacenterId()), 0L);
				}
			}
		}
		return orderIdWork;
	}

	public IdWorkerUtil getOutTradeIdWork() {
		if (outTradeIdWork == null) {
			synchronized (this) {
				if (outTradeIdWork == null) {
					outTradeIdWork = new IdWorkerUtil(Integer.valueOf(propUtil.getWorkerId()),
							Integer.valueOf(propUtil.getDatacenterId()), 0L);
				}
			}
		}
		return outTradeIdWork;
	}

	public IdWorkerUtil getCVOrderIdWork() {
		if (cvOrderIdWork == null) {
			synchronized (this) {
				if (cvOrderIdWork == null) {
					cvOrderIdWork = new IdWorkerUtil(Integer.valueOf(propUtil.getWorkerId()),
							Integer.valueOf(propUtil.getDatacenterId()), 0L);
				}
			}
		}
		return cvOrderIdWork;
	}

	public IdWorkerUtil getCVUserOrderIdWork() {
		if (cvUserOrderIdWork == null) {
			synchronized (this) {
				if (cvUserOrderIdWork == null) {
					cvUserOrderIdWork = new IdWorkerUtil(Integer.valueOf(propUtil.getWorkerId()),
							Integer.valueOf(propUtil.getDatacenterId()), 0L);
				}
			}
		}
		return cvUserOrderIdWork;
	}
	
	public IdWorkerUtil getCVOutTradeIdWork() {
		if (cvOutTradeIdWork == null) {
			synchronized (this) {
				if (cvOutTradeIdWork == null) {
					cvOutTradeIdWork = new IdWorkerUtil(Integer.valueOf(propUtil.getWorkerId()),
							Integer.valueOf(propUtil.getDatacenterId()), 0L);
				}
			}
		}
		return cvOutTradeIdWork;
	}

}
