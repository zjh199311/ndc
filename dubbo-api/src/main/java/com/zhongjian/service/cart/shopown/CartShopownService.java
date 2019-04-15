package com.zhongjian.service.cart.shopown;

import com.zhongjian.dto.common.ResultDTO;

/**
 * @Author: ldd
 */
public interface CartShopownService {

    /**
     * 购物车显示列表
     */
    ResultDTO<Object> queryList(Integer uid);


}
