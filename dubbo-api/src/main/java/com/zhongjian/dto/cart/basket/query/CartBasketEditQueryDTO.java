package com.zhongjian.dto.cart.basket.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class CartBasketEditQueryDTO implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * basketId
     */
    private Integer id;

    /**
     * 菜品id
     */
    private Integer gid;

    /**
     * 用户登入uid
     */
    private Integer uid;

    /**
     * 商家id
     */
    private Integer sid;

    /**
     * 数量
     */
    private String amount;

    /**
     * 备注
     */
    private String remark;



    //----------------------当用户点击其他的时候传入该字段

    //价格
    private String price;
}

