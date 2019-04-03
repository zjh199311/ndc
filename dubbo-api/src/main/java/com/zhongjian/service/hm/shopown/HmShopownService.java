package com.zhongjian.service.hm.shopown;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.shopown.query.HmShopownStatusQueryDTO;
import com.zhongjian.dto.hm.shopown.result.HmShopownResultDTO;

/**
 * @Author: ldd
 */
public interface HmShopownService {

    /**
     * 购物车显示列表
     */
    ResultDTO<HmShopownResultDTO> queryList(String loginToken);
    
    /**
     * 判断所有商铺是不是指定状态
     * @param hmShopownStatusQueryDTO
     * @return
     */
    ResultDTO<Boolean> judgeHmShopownStatus(HmShopownStatusQueryDTO hmShopownStatusQueryDTO);
}
