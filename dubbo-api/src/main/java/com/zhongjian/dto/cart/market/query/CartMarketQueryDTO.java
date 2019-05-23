package com.zhongjian.dto.cart.market.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class CartMarketQueryDTO implements Serializable{

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 菜场Id
     */
    private Integer marketId;

    /**
     * 用户id
     */
    private Integer uid;
}
