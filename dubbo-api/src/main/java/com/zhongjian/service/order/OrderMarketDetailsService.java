package com.zhongjian.service.order;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.order.order.query.OrderQueryDTO;

/**
 * @Author: ldd
 */
public interface OrderMarketDetailsService {

    ResultDTO<Object> orderDetails(OrderQueryDTO orderQueryDTO);
}
