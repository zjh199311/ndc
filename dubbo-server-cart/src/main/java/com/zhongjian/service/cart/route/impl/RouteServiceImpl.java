package com.zhongjian.service.cart.route.impl;

import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.dao.entity.cart.shopown.CartShopownBean;
import com.zhongjian.dao.framework.impl.HmBaseService;
import com.zhongjian.dto.cart.route.BasketCVSidDTO;
import com.zhongjian.service.cart.route.RouteService;
import org.springframework.stereotype.Service;

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

        basketCVSidDTO.setBasketSids(remove(basketSids));
        basketCVSidDTO.setCvSids(remove(cvSids));

        return basketCVSidDTO;
    }

    public static int[] remove(int[] sid) {
        // 定义数组
        int h = 0; // 设置一个变量作为增量
        // 循环读取oldArr数组的值
        for (int b : sid) {
            // 判断，如果oldArr数组的值不为0那么h就加1
            if (b != 0) {
                h++;
            }
        }
        // 得到了数组里不为0的个数，以此个数定义一个新数组，长度就是h
        int newArr[] = new int[h];
        // 这里偷个懒，不想从新定义增量了，所以把增量的值改为0
        h = 0;
        // 在次循环读取oldArr数组的值
        for (int c : sid) {
            // 把不为0的值写入到newArr数组里面
            if (c != 0) {
                newArr[h] = c;
                h++;// h作为newArr数组的下标，没写如一个值，下标h加1
            }
        }
        return newArr;
    }
}

