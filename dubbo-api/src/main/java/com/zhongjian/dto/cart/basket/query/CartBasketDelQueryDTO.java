package com.zhongjian.dto.cart.basket.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: yd
 */
@Data
public class CartBasketDelQueryDTO implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * basketId
     */
    private Integer id;

    /**
     * 用户uid
     */
    private Integer uid;

    /**
     * 商户id
     */
    private Integer sid;

    /**
     * 多个商户id
     */
    private int[] sids;


}
