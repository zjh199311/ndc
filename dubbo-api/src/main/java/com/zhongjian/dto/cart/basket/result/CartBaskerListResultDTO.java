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
    @SerializedName(value = "carts")
    List<CartBasketResultDTO> cartBasketResultDTOS;

    /**
     * 总价格
     */
    private String totalPrice;

    /**
     * 优惠价
     */
    private String totalDisPrice;



}
