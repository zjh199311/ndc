package com.zhongjian.dto.cart.basket.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class CartBasketListQueryDTO implements Serializable{

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 商户id
     */
    private Integer sid;

    /**
     * 用户id
     */
    private Integer uid;
}
