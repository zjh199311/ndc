package com.zhongjian.dto.cart.marketActivity.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class CartMarketActivityResultDTO implements Serializable{

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 活动描述
     */
    private String rule;

    /**
     * 活动类型0满减1折扣
     */
    private Integer type;

    /**
     * 状态
     */
    private String status;

    /**
     * 最大限额
     */
    private Integer upLimit;
}
