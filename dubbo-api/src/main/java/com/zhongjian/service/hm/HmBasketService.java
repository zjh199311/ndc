package com.zhongjian.service.hm;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.query.HmBasketQueryDTO;

/**
 * @Author: ldd
 */
public interface HmBasketService {

    /**
     * 新增和修改功能
     * @param hmBasketQueryDTO
     * @return
     */
    ResultDTO<Boolean> addOrUpdateInfo(HmBasketQueryDTO hmBasketQueryDTO);

}
