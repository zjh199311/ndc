package com.zhongjian.localservice.impl;

import com.zhongjian.dao.entity.order.rider.OrderRiderOrderBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.localservice.OrderService;
import com.zhongjian.util.DateUtil;
import com.zhongjian.util.LogUtil;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

@Service("localOrderService")
public class OrderServiceImpl extends HmBaseService<OrderRiderOrderBean, Integer> implements OrderService {


    @Resource
    com.zhongjian.service.order.OrderService orderService;

    @Override
    public void todoSth() {

        List<Integer> ids = this.dao.executeListMethod(null, "findRiderOrder", Integer.class);
       
        LogUtil.info("定时任务" , DateUtil.formateDate(new Date(),DateUtil.DateMode_4) + ":处理超时订单任务开始");
        for (Integer id : ids) {
            orderService.cancelOrder(id);
        }
        LogUtil.info("定时任务" , DateUtil.formateDate(new Date(),DateUtil.DateMode_4) + ":处理超时订单任务结束");
    }
}
