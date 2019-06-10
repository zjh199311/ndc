package com.zhongjian.dto.cart.basket.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class CartBasketResultDTO implements Serializable {

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

    /**
     * 食品名称
     */
    private String gname;

    /**
     * 商品描述
     */
    private String content;

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

    /**
     * 要是食品为其他的话. 给一个标识status
     */

    private Integer status;


}
