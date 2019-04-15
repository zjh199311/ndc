package com.zhongjian.service.cart.basket;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.cart.basket.query.HmBasketDelQueryDTO;
import com.zhongjian.dto.cart.basket.query.HmBasketEditQueryDTO;
import com.zhongjian.dto.cart.basket.query.HmBasketListQueryDTO;

/**
 * @Author: ldd
 */
public interface CartBasketService {

    /**
     * 新增和叠加功能
     *
     * @param hmBasketEditQueryDTO
     * @return
     */
    ResultDTO<Object> addOrUpdateInfo(HmBasketEditQueryDTO hmBasketEditQueryDTO);

    /**
     * 商户在商家里面的购物车详情页
     */
    ResultDTO<Object> queryList(HmBasketListQueryDTO hmBasketListQueryDTO);

    /**
     * 根据主键id和用户id删除订单
     */
    ResultDTO<Object> deleteInfoById(HmBasketDelQueryDTO hmBasketDelQueryDTO);

    /**
     * 清空该用户在该商家下所有购物订单
     */
    ResultDTO<Object> deleteAllInfoById(HmBasketDelQueryDTO hmBasketDelQueryDTO);

    /**
     * 编辑功能(商户购物车里面的编辑,以及购物车的加减)
     */
    ResultDTO<Object> editInfo(HmBasketEditQueryDTO hmBasketEditQueryDTO);


    /**
     * 款后删除用户在菜场下对应的所有商户付
     */
    ResultDTO<Object> deleteAllByPid(HmBasketDelQueryDTO hmBasketDelQueryDTO);


}
