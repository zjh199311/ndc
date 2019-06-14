package com.zhongjian.localservice.impl;

import com.zhongjian.commoncomponent.PropUtil;
import com.zhongjian.dao.entity.order.rider.OrderRiderOrderBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dao.jdbctemplate.CVOrderDao;
import com.zhongjian.localservice.OrderService;
import com.zhongjian.task.OrderTask;
import com.zhongjian.util.DateUtil;
import com.zhongjian.util.DistributedLock;
import com.zhongjian.util.LogUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

@Service("localOrderService")
public class OrderServiceImpl extends HmBaseService<OrderRiderOrderBean, Integer> implements OrderService {


    @Resource
    private com.zhongjian.service.order.OrderService orderService;
    
    @Resource
    private CVOrderDao cvOrderDao;
    
    @Resource
    private PropUtil propUtil;
    
    @Resource
    private DistributedLock zkLock;
    
    @Resource
    private OrderTask orderTask;
    
    @Override
    public void todoSth() {

        List<Integer> ids = this.dao.executeListMethod(null, "findRiderOrder", Integer.class);
       
        LogUtil.info("定时任务" , DateUtil.formateDate(new Date(),DateUtil.DateMode_4) + ":处理超时订单任务开始");
        for (Integer id : ids) {
            orderService.cancelOrder(id);
        }
        LogUtil.info("定时任务" , DateUtil.formateDate(new Date(),DateUtil.DateMode_4) + ":处理超时订单任务结束");
    }

	@Override
	public boolean changeDelieverModel(Integer uoid) {
		if (cvOrderDao.changeModelToTwo(uoid)) {
			return true;
		} 
		if (cvOrderDao.getDeliverModelByUoid(uoid) == 2) {
			return true;
		}
		return false;
	}

	
	
	
	@Override
	@Transactional
	public void distributeOrder(Integer uoid,boolean isFirst,int time) {
		//派单的前置条件rid等于0
		if (time > 8) {
			//超过八次直接拒绝（菜场无骑手）
			return;
		}
		if (!isFirst) {
			try {
				Thread.sleep(60 * 5 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (cvOrderDao.getRidFromCVOrder(uoid) != 0) {
			return;
		}
		Integer marketId = 0;
		// 查出makertid 
		marketId = cvOrderDao.getMarketIdByCVOrder(uoid);
		if (marketId == 0) {
			return;
		}
		//派单逻辑
		Integer rid = orderService.getRidFormMarket(marketId, "least");
		if (rid != -1) {
			cvOrderDao.updateRidOfHmCVOrder(rid, uoid);
			cvOrderDao.deleteWaitdeliverOrder(uoid);
			orderTask.handleCVOrderPushRider(rid);
		}else {
		//重新派单
				distributeOrder(uoid,false,time ++);
		}
		
	}
	
	public void changeAndDistributeOrder(Integer uoid) {
		if (this.changeDelieverModel(uoid)) {
			this.lockDistributeOrder(uoid);
		}
	}

	@Override
	public Integer getDelieverModel(Integer uoid) {
		return cvOrderDao.getDeliverModelByUoid(uoid);
	}

	@Override
	public List<Integer> queryWaitdeliverOrderList() {
		return cvOrderDao.queryWaitdeliverOrderList(propUtil.getDatacenterId());
	}

	@Override
	public void lockDistributeOrder(Integer uoid) {
		String lockName = null;
		try {
			//zookeeper加锁(针对uid加锁)
			try {
				lockName = zkLock.lock(uoid + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.distributeOrder(uoid,true,1);
		} finally {
			//zookeeper解锁
			try {
				zkLock.unlock(lockName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
