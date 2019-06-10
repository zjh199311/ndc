package com.zhongjian.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: ldd
 */
@AllArgsConstructor
public enum CvorderEnum {

    TO_BE_PAID("订单待支付","TO_BE_PAID"),

    ORDER_DISTRIBUTION("订单配货中","ORDER_DISTRIBUTION"),

    BEING_DISPATCHED("订单正在配送","BEING_DISPATCHED"),

    COMPLETED("订单已完成","COMPLETED"),

    SELF_MENTION("订单自提","SELF_MENTION"),

    EVALUATION_COMPLETED("订单已评价","EVALUATION_COMPLETED"),

    ORDER_RESERVATION("订单预约中","ORDER_RESERVATION"),

    VIEW_RIDERS("查看骑手","VIEW_RIDERS"),

    EVALUATE_IT("评价一下","EVALUATE_IT"),

    IMMEDIATE_PAYMENT("立即支付","IMMEDIATE_PAYMENT");
    ;


    private @Getter String msg;

    private @Getter String code;





}
