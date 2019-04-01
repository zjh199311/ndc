package com.zhongjian.dto.hm.basket.result;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Author: ldd
 */
@Data
public class HmBasketResultDTO implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * basketId
     */
    private Integer id;

    /**
     * 商品名称
     */
    private String foodName;

    /**
     * 商品总价
     */
    private String totalPrice;

    /**
     * 商品数量
     */
    private String amount;

    /**
     * 商户名称
     */
    private String shopName;

    /**
     * 优惠活动
     */
    private String activities;

    /**
     * 备注
     */
    private String remark;

    /**
     * 优惠数值
     */
    private String reduce;

    /**
     * 满减额度
     */
    private String full;

    /**
     * '类型0满减1折扣'
     */
    private Integer type;

    /**
     * 商户图片
     */
    private String picture;


}
