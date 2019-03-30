package com.zhongjian.service.hm;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.hm.query.HmBasketDelQueryDTO;
import com.zhongjian.dto.hm.query.HmBasketEditQueryDTO;
import com.zhongjian.dto.hm.query.HmBasketListQueryDTO;
import com.zhongjian.dto.hm.result.HmBasketResultDTO;

import java.util.List;

/**
 * @Author: ldd
 */
public interface HmBasketService {

    /**
     * 新增和叠加功能
     * @param hmBasketEditQueryDTO
     * @return
     */
    ResultDTO<Boolean> addOrUpdateInfo(HmBasketEditQueryDTO hmBasketEditQueryDTO);

    /**
     * 商户在商家里面的购物车详情页
     */
    ResultDTO<List<HmBasketResultDTO>> queryListById(HmBasketListQueryDTO hmBasketListQueryDTO);

    /**
     * 根据主键id和用户id删除订单
     */
    ResultDTO<Boolean> deleteInfoById(HmBasketDelQueryDTO hmBasketDelQueryDTO);

    /**
     * 清空该用户在该商家下所有购物订单
     */
    ResultDTO<Boolean>deleteAllInfoById(HmBasketDelQueryDTO hmBasketDelQueryDTO);

    /**
     * 编辑功能(商户购物车里面的编辑,以及购物车的加减)
     */
    ResultDTO<Boolean> editInfo(HmBasketEditQueryDTO hmBasketEditQueryDTO);







}
