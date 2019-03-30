package com.zhongjian.dto.hm.result;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

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
    private BigDecimal totalPrice;

    /**
     * 商品数量
     */
    private BigDecimal amount;
}
