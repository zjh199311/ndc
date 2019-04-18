package com.zhongjian.service.order;

import java.util.Map;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.order.address.query.OrderAddressQueryDTO;
import com.zhongjian.dto.order.address.result.OrderAddressResultDTO;
import com.zhongjian.dto.order.order.query.OrderStatusQueryDTO;

public interface OrderService {

    //type 0选用积分 extra=null 1选用优惠券 extra = couponId
    Map<String, Object> previewOrCreateOrder(Integer uid, Integer sids[], String type,
                                             Integer extra, String isSelfMention, boolean toCreateOrder,
                                             Integer addresId, Integer unixTime, Integer isAppointment);

    /**
     * 判断所有商铺是不是指定状态
     *
     * @param orderStatusQueryDTO
     * @return
     */
    ResultDTO<Object> judgeHmShopownStatus(OrderStatusQueryDTO orderStatusQueryDTO);

    /**
     * 获取预约时间
     *
     * @param isAppointment
     * @return
     */
    String previewOrderTime(Integer isAppointment);

    Map<String, Object> getOutTradeNoAndAmount(Integer uid, Integer orderId, String business);


}
