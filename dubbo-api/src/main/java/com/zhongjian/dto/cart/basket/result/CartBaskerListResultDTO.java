package com.zhongjian.dto.cart.basket.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @Author: ldd
 */
@Data
public class CartBaskerListResultDTO implements Serializable{

    private static final long serialVersionUID = 197018972999527001L;


    /**
     * 食品信息
     */
    List<CartBasketResultDTO> carts;

    /**
     * 总价格
     */
    private String totalPrice;

    /**
     * 起步价
     */
    private String startingPrice;

    /**
     * 差价
     */
    private String disparity;

    /**
     * 状态 1达到.0未达到
     */
    private Integer status;

    /**
     * 优惠价
     */
    private String totalDisPrice;



}
