package com.zhongjian.dto.order.order.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class OrderQueryDTO implements Serializable {

    private static final long serialVersionUID = -1728911295677881024L;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 订单id
     */
    private Integer roid;
}
