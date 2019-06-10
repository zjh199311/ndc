package com.zhongjian.dao.entity.order.cvstore;

import java.math.BigDecimal;

public class OrderCvUserOrderBean {
    /**
     * 
     */
    private Integer id;

    /**
     * 订单序号
     */
    private String orderSn;

    /**
     * 支付状态0待支付1已支付
     */
    private Byte payStatus;

    /**
     * 
     */
    private Integer uid;

    /**
     * 积分抵扣金额
     */
    private BigDecimal integralprice;

    /**
     * 商户活动减免(额外记录商户活动减免)
     */
    private BigDecimal storeActivityPrice;

    /**
     * vip减免
     */
    private BigDecimal vipRelief;

    /**
     * 优惠券额度
     */
    private BigDecimal couponPrice;

    /**
     * 优惠券id
     */
    private Integer couponId;

    /**
     * 用户是否显示该单（完成的订单增加删除）1显示0删除
     */
    private Byte isShow;

    /**
     * 订单实付
     */
    private BigDecimal totalprice;

    /**
     * 原价(商户该得的金额)
     */
    private BigDecimal originalprice;

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 订单序号
     * @return order_sn 订单序号
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * 订单序号
     * @param orderSn 订单序号
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn == null ? null : orderSn.trim();
    }

    /**
     * 支付状态0待支付1已支付
     * @return pay_status 支付状态0待支付1已支付
     */
    public Byte getPayStatus() {
        return payStatus;
    }

    /**
     * 支付状态0待支付1已支付
     * @param payStatus 支付状态0待支付1已支付
     */
    public void setPayStatus(Byte payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * 
     * @return uid 
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * 
     * @param uid 
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * 积分抵扣金额
     * @return integralPrice 积分抵扣金额
     */
    public BigDecimal getIntegralprice() {
        return integralprice;
    }

    /**
     * 积分抵扣金额
     * @param integralprice 积分抵扣金额
     */
    public void setIntegralprice(BigDecimal integralprice) {
        this.integralprice = integralprice;
    }

    /**
     * 商户活动减免(额外记录商户活动减免)
     * @return store_activity_price 商户活动减免(额外记录商户活动减免)
     */
    public BigDecimal getStoreActivityPrice() {
        return storeActivityPrice;
    }

    /**
     * 商户活动减免(额外记录商户活动减免)
     * @param storeActivityPrice 商户活动减免(额外记录商户活动减免)
     */
    public void setStoreActivityPrice(BigDecimal storeActivityPrice) {
        this.storeActivityPrice = storeActivityPrice;
    }

    /**
     * vip减免
     * @return vip_relief vip减免
     */
    public BigDecimal getVipRelief() {
        return vipRelief;
    }

    /**
     * vip减免
     * @param vipRelief vip减免
     */
    public void setVipRelief(BigDecimal vipRelief) {
        this.vipRelief = vipRelief;
    }

    /**
     * 优惠券额度
     * @return coupon_price 优惠券额度
     */
    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    /**
     * 优惠券额度
     * @param couponPrice 优惠券额度
     */
    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

    /**
     * 优惠券id
     * @return coupon_id 优惠券id
     */
    public Integer getCouponId() {
        return couponId;
    }

    /**
     * 优惠券id
     * @param couponId 优惠券id
     */
    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    /**
     * 用户是否显示该单（完成的订单增加删除）1显示0删除
     * @return is_show 用户是否显示该单（完成的订单增加删除）1显示0删除
     */
    public Byte getIsShow() {
        return isShow;
    }

    /**
     * 用户是否显示该单（完成的订单增加删除）1显示0删除
     * @param isShow 用户是否显示该单（完成的订单增加删除）1显示0删除
     */
    public void setIsShow(Byte isShow) {
        this.isShow = isShow;
    }

    /**
     * 订单实付
     * @return totalPrice 订单实付
     */
    public BigDecimal getTotalprice() {
        return totalprice;
    }

    /**
     * 订单实付
     * @param totalprice 订单实付
     */
    public void setTotalprice(BigDecimal totalprice) {
        this.totalprice = totalprice;
    }

    /**
     * 原价(商户该得的金额)
     * @return originalPrice 原价(商户该得的金额)
     */
    public BigDecimal getOriginalprice() {
        return originalprice;
    }

    /**
     * 原价(商户该得的金额)
     * @param originalprice 原价(商户该得的金额)
     */
    public void setOriginalprice(BigDecimal originalprice) {
        this.originalprice = originalprice;
    }
}