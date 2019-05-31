package com.zhongjian.service.order;

import java.util.Map;


public interface CVOrderService {
	
	// type 1选用积分 extra=null 2选用优惠券 extra = couponId
	Map<String, Object> previewCVOrder(Integer uid, Integer sid, String type, Integer extra);

	
	  /**
       ** 获取预约时间
       */
    String previewOrderTime();
	
    //支付状态变更
    boolean cancelOrder(Integer orderId);
    
    /**
     ** 生成订单
     */
    
    Map<String, Object> createOrder(Integer uid, Integer sid, String type, Integer extra,
    		String isSelfMention,Integer addressId, Integer unixTime);
    
    /**
     ** 是否已打烊
     */
    boolean judgeHmShopownStatus(Integer sid);
}
