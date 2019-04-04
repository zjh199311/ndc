package com.zhongjian.dto.order.reult;

import java.io.Serializable;
import java.util.List;

import com.zhongjian.dto.order.address.result.PreviewOrderAddressResultDTO;
import com.zhongjian.dto.order.shopown.result.PreviewOrderShopownResultDTO;

import lombok.Data;

@Data
public class PreviewOrderResultDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 258410785951927885L;
    
    /**
     * 配送时间
     */
    private String deliveryTime;
    
    /**
     * 收货地址
     */
    private PreviewOrderAddressResultDTO addressResultDTO;
    
    /**
     * 店铺订单详情
     */
    private List<PreviewOrderShopownResultDTO> shopownResultDTOs;
    
    /**
     * 所选的活动类型  0 未选择活动  1 积分  2 优惠券  3 菜场满减
     */
    private Integer discountsType;
    
    /**
     * 活动减免金额
     */
    private String discountsPrice;
    
    /**
     * 订单应付金额
     */
    private String totalPrice;
    
    /**
     * 配送费
     */
    private String deliveryPrice;
    
    /**
     * 配送类型 0 配送 1 自提
     */
    private Integer deliveryType;
    
    /**
     * 是否会员 0 否  1 是
     */
    private Integer vip;
    
    /**
     * 开通会员减免金额。是会员就没有
     */
    private String vipDiscountsPrice;
    
    /**
     * 最终支付应付的总金额
     */
    private String payPrice;

}
