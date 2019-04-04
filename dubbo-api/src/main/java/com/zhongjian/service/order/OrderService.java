package com.zhongjian.service.order;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.order.reult.PreviewOrderResultDTO;

public interface OrderService {

	//type 0选用积分 extra=null 1选用优惠券 extra = couponId 
	ResultDTO<PreviewOrderResultDTO> previewOrder(Integer uid,Integer sids[],Integer isAppointment,String type,String extra,String isSelfMention);
	
}
