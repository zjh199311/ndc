package com.zhongjian.service.order.hm.shopown;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.shopown.result.HmShopownResultDTO;
import com.zhongjian.dto.order.hm.shopown.query.HmShopownStatusQueryDTO;

/**
 * @Author: xc
 */
public interface OrderHmShopownService {

    
    /**
     * 判断所有商铺是不是指定状态
     * @param hmShopownStatusQueryDTO
     * @return
     */
    ResultDTO<Boolean> judgeHmShopownStatus(HmShopownStatusQueryDTO hmShopownStatusQueryDTO);
}
