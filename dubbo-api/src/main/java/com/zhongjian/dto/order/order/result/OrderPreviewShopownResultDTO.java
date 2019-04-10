package com.zhongjian.dto.order.order.result;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderPreviewShopownResultDTO implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2548785476444048539L;
    
    /**
     * 店铺id
     */
    private Integer pid;
    
    
    /**
     * 店铺名称
     */
    private String sname;
    
    /**
     * 商品总价
     */
    private String totalPrice;
    
    /**
     * 优惠金额
     */
    private String discountsPrice;
    
    /**
     * 活动后商品应付总价
     */
    private String payPrice;

}
