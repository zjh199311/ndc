package com.zhongjian.service.hm.basket;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.basket.query.HmBasketDelQueryDTO;
import com.zhongjian.dto.hm.basket.query.HmBasketEditQueryDTO;
import com.zhongjian.dto.hm.basket.query.HmBasketListQueryDTO;
import com.zhongjian.dto.hm.basket.result.HmBasketResultDTO;

import java.util.List;

/**
 * @Author: ldd
 */
public interface HmBasketService {

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


}
