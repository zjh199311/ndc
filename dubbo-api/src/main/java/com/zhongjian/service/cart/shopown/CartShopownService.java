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

    /**
     * 根据uid删除已下架的商品
     *
     * @param uid
     * @param flag
     * @return
     */
    void deleteGoodsOnShelves(Integer uid, Boolean flag);
}
