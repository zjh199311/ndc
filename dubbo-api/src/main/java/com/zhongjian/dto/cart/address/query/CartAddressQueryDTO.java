package com.zhongjian.dto.cart.address.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class CartAddressQueryDTO implements Serializable {

    private static final long serialVersionUID = -5948092855255306532L;

    /**
     * address主键id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * marketId
     */
    private Integer marketId;


}
