package com.zhongjian.service.order;

import com.zhongjian.dto.Page;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.order.order.query.OrderQueryDTO;

/**
 * @Author: ldd
 */
public interface OrderDetailsService {


    /**
     * 订单详情接口
     *
     * @param orderQueryDTO
     * @return
     */
    ResultDTO<Object> queryList(OrderQueryDTO orderQueryDTO, Page page);

}
