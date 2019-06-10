package com.zhongjian.dto.order.order.result;

import com.zhongjian.dto.cart.basket.result.CartBasketResultDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ldd
 */
@Data
public class OrderDetailsResultDTO implements Serializable {

    private static final long serialVersionUID = -2548785476444048539L;


    /**
     * 主键Id
     */
    private Integer roid;


    /**
     * 配送订单
     */
    private String riderSn;

    /**
     * 类型1便利店 0 菜场
     */
    private Integer type;

    /**
     * 骑手状态
     */
    private Integer riderStatus;

    /**
     * 支付状态
     */
    private Integer payStatus;

    /**
     * 预约
     */
    private Integer isAppointment;

    /**
     * 总价
     */
    private String price;

    /**
     * 创建时间
     */
    private Integer ctime;

    /**
     * 创建时间格式化
     */
    private String time;

    /**
     * 订单状态描述
     */
    private String statusContent;

    /**
     *订单操作按钮内容
     */
    private String buttonContent;

    /**
     * 订单状态: 0配货中 1配送中 2完成 3 自提 4已评价 5预约 6待支付
     */
    private Integer status;

    /**
     * 1显示... 0不显示
     */
    private Integer showMore;

    /**
     * 食品
     */
    List<CartBasketResultDTO> list;
}
