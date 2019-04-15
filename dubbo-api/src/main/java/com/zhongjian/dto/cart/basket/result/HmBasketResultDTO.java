package com.zhongjian.dto.cart.basket.result;

import lombok.Data;

import java.io.Serializable;

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
     * 商户id
     */
    private Integer sid;

    /**
     * 商品数量
     */
    private String amount;
    /**
     * 商品总价
     */
    private String totalPrice;

    /**
     * 商品价
     */
    private String price;

    /**
     * 商品名称
     */
    private String foodName;

    //------------------------

    /**
     * 商品原价
     */
    private String unitPrice;

    /**
     * 商品描述
     */
    private String remark;

    /**
     * 商品单位
     */
    private String unit;

    /**
     * 商品id
     */
    private Integer gid;


}
