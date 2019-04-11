package com.zhongjian.service.order;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.order.address.query.OrderAddressQueryDTO;
import com.zhongjian.dto.order.address.result.OrderAddressResultDTO;
import com.zhongjian.dto.order.order.query.OrderStatusQueryDTO;

public interface OrderService {

    //type 0选用积分 extra=null 1选用优惠券 extra = couponId
    ResultDTO<Object> previewOrder(Integer uid, Integer sids[] , String type, String extra, String isSelfMention);

    /**
     * 判断所有商铺是不是指定状态
     *
     * @param orderStatusQueryDTO
     * @return
     */
    ResultDTO<Object> judgeHmShopownStatus(OrderStatusQueryDTO orderStatusQueryDTO);

    OrderAddressResultDTO previewOrderAddress(OrderAddressQueryDTO orderAddressQueryDTO);

    //0 "16:20"  1 "04-11 08:48"
    String previewOrderTime(Integer isAppointment);


}
