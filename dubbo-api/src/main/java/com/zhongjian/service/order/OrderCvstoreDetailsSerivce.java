package com.zhongjian.service.order;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.order.order.query.OrderQueryDTO;

/**
 * @Author: ldd
 */
public interface OrderCvstoreDetailsSerivce {


    ResultDTO<Object> orderDetails(OrderQueryDTO orderQueryDTO);

}

