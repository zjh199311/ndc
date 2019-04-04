package com.zhongjian.service.order;

import com.zhongjian.dto.common.ResultDTO;

public interface OrderService {

	//type 0选用积分 extra=null 1选用优惠券 extra = couponId 
	ResultDTO<?> previewOrder(Integer uid,Integer sids[],Integer isAppointment,String type,String extra,String isSelfMention);
	
}
