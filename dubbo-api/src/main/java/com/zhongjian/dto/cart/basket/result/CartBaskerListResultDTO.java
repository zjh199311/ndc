package com.zhongjian.dto.cart.basket.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ldd
 */
@Data
public class CartBaskerListResultDTO implements Serializable{

    private static final long serialVersionUID = 197018972999527001L;


    /**
     * 食品信息
     */
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
