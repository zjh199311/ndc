package com.zhongjian.dto.cart.market.result;

import com.zhongjian.dto.cart.marketActivity.result.CartMarketActivityResultDTO;
import com.zhongjian.dto.cart.shopown.result.CartShopownResultDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ldd
 */
@Data
public class CartMarketResultByAdvenceDTO implements Serializable{

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 菜场id
     */
    private Integer martketId;

    /**
     * 菜场名称
     */
    private String marketName;

    /**
     * 全部价格
     */
    private String totalPrice;

    /**
     * 活动描述
     */
    private String rule;

    /**
     * 店铺类型2.预约中 1.打烊 0开张
     */
    private Integer type;

    /**
     * 状态
     */
    private String status;

    /**
     * 用户在商户下购买的信息
     */
    private List<CartShopownResultDTO> shopown;

    /**
     * 菜场活动
     */
    CartMarketActivityResultDTO marketActivity;
}
