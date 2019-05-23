package com.zhongjian.dto.cart.storeActivity.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class CartCvStoreActivityResultDTO implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 起步价
     */
    private String startingPrice;

    /**
     * 商户id
     */
    private Integer sid;
}
