package com.zhongjian.dto.cart.shopown.result;

import com.zhongjian.dto.cart.basket.result.HmBasketResultDTO;
import com.zhongjian.dto.cart.storeActivity.result.HmStoreActivityResultDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ldd
 */
@Data
public class HmShopownResultByCloseDTO implements Serializable{

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 商户id
     */
    private Integer pid;
    /**
     * 商户图片
     */
    private String picture;

    /**
     * 商户名称
     */
    private String shopName;

    /**
     * 商品总价
     */
    private String totalPrice;

    /**
     * 优惠后价格
     */
    private String discountPrice;

    /**
     * 状态
     */
    private String status;


    /**
     * 状态描述
     */
    private String statusMsg;

    /**
     * 活动描述
     */
    private String activityMsg;

    /**
     * 商家下对应的活动信息
     */
    List<HmStoreActivityResultDTO> hmStoreActivityResultDTOS;

    /**
     * 该用户在商家下对应的食品信息
     */
    List<HmBasketResultDTO> hmBasketResultDTOS;

    /**
     * 备注组合
     */
    private List<String> remarkList;
}
