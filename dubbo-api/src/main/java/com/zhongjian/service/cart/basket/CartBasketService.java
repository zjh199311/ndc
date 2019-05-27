package com.zhongjian.service.cart.basket;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.cart.basket.query.CartBasketDelQueryDTO;
import com.zhongjian.dto.cart.basket.query.CartBasketEditQueryDTO;
import com.zhongjian.dto.cart.basket.query.CartBasketListQueryDTO;

/**
 * @Author: ldd
 */
public interface CartBasketService {

    /**
     * 新增和叠加功能
     *
     * @param cartBasketEditQueryDTO
     * @return
     */
    ResultDTO<Object> addOrUpdateInfo(CartBasketEditQueryDTO cartBasketEditQueryDTO);

    /**
     * 商户在商家里面的购物车详情页
     */

    ResultDTO<Object> queryList(CartBasketListQueryDTO cartBasketListQueryDTO);

    /**
     * 根据主键id和用户id删除订单
     */
    ResultDTO<Object> deleteInfoById(CartBasketDelQueryDTO cartBasketDelQueryDTO);

    /**
     * 清空该用户在该商家下所有购物订单
     */

    ResultDTO<Object> deleteAllInfoById(CartBasketDelQueryDTO cartBasketDelQueryDTO);

    /**
     * 编辑功能(商户购物车里面的编辑,以及购物车的加减)
     */
    ResultDTO<Object> editInfo(CartBasketEditQueryDTO cartBasketEditQueryDTO);


    /**
     * 款后删除用户在菜场下对应的所有商户付
     */
    ResultDTO<Object> deleteAllByPid(CartBasketDelQueryDTO cartBasketDelQueryDTO);


}
