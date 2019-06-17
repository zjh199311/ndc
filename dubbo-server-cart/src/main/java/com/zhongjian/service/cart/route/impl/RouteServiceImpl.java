package com.zhongjian.service.cart.route.impl;

import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.dao.entity.cart.shopown.CartShopownBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dto.cart.route.BasketCVSidDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.service.cart.route.RouteService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @Author: ldd
 */
@Service
public class RouteServiceImpl extends HmBaseService<CartShopownBean, Integer> implements RouteService {
    @Override
    public BasketCVSidDTO devideSid(int[] sids) {

        BasketCVSidDTO basketCVSidDTO = new BasketCVSidDTO();
        int[] basketSids = new int[sids.length];
        int[] cvSids = new int[sids.length];

        for (int i = 0; i < sids.length; i++) {
            int sid = sids[i];
            CartShopownBean findShopownModeById = this.dao.executeSelectOneMethod(sid, "findShopownModeById", CartShopownBean.class);
            //0为菜场, 1为便利店
            if (FinalDatas.ZERO.equals(findShopownModeById.getMode())) {
                basketSids[i] = sids[i];
            } else {
                cvSids[i] = sids[i];
            }
        }
        if (basketSids[basketSids.length - 1] == 0) {
            basketSids = Arrays.copyOf(basketSids, basketSids.length - 1);
        }
        if (cvSids[cvSids.length - 1] == 0) {
            cvSids = Arrays.copyOf(cvSids, cvSids.length - 1);
        }
        basketCVSidDTO.setBasketSids(basketSids);
        basketCVSidDTO.setCvSids(cvSids);

        return basketCVSidDTO;
    }

}

