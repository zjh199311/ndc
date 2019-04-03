package com.zhongjian.service.hm.shopown;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.shopown.result.HmShopownResultDTO;
import com.zhongjian.dto.order.hm.shopown.query.HmShopownStatusQueryDTO;

/**
 * @Author: ldd
 */
public interface HmShopownService {

    /**
     * 购物车显示列表
     */
    ResultDTO<HmShopownResultDTO> queryList(String loginToken);
    
  
}
