package com.zhongjian.dto.order.order.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class OrderCvOrderResultDTO implements Serializable {

    private static final long serialVersionUID = -2548785476444048539L;

    /**
     * 地址id
     */
    private Integer addressId;

    /**
     * 配送费
     */
    private String deliverFee;

    /**
     * 预约时间时间戳
     */
    private Integer time;

    /**
     * 预约时间
     */
    private String serviceTime;

    /**
     * 订单状态
     */
    private Integer riderStatus;

    /**
     * 支付状态
     */
    private Integer payStatus;

    /**
     * 现价
     */
    private String payment;

    /**
     * 原价
     */
    private String total;


}
