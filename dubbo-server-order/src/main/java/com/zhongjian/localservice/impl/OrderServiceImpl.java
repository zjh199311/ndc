package com.zhongjian.localservice.impl;

import com.zhongjian.dao.entity.order.rider.OrderRiderOrderBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.localservice.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderServiceImpl extends HmBaseService<OrderRiderOrderBean, Integer> implements OrderService {


    @Resource
    com.zhongjian.service.order.OrderService orderService;

    @Override
    public void todoSth() {

        List<Integer> id = this.dao.executeListMethod(null, "findRiderOrder", Integer.class);
        for (Integer integer : id) {
            orderService.cancelOrder(integer);
        }
    }
}
