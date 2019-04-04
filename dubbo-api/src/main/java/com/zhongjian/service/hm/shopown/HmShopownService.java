package com.zhongjian.service.hm.shopown;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.market.result.HmMarketResultListDTO;

/**
 * @Author: ldd
 */
public interface HmShopownService {

    /**
     * 购物车显示列表
     */
    ResultDTO<HmMarketResultListDTO> queryList(String loginToken);


}
