package com.zhongjian.dao.entity.cart.rider;

import java.math.BigDecimal;

public class CartRiderOrderBean {
    /**
     * 
     */
    private Integer id;

    /**
     * 配送订单
     */
    private String riderSn;

    /**
     * 配送的所有订单
     */
    private String orderSn;

    /**
     * 待接单、1配送中、2已送达、3自提、4已评价
     */
    private Byte riderStatus;

    /**
     * 待支付、1支付成功、2支付失败
     */
    private Byte payStatus;

    /**
     * 
     */
    private Integer uid;

    /**
     * 配送人员
     */
    private Integer rid;

    /**
     * 菜场id
     */
    private Integer marketid;

    /**
     * 配送费用
     */
    private BigDecimal riderPay;

    /**
     * 配送地址
     */
    private Integer addressId;

    /**
     * 用户优惠券id
     */
    private Integer couponid;

    /**
     * 支付方式
     */
    private String typePay;

    /**
     * 
     */
    private Double totalprice;

    /**
     * 订单支付时间
     */
    private Integer payTime;

    /**
     * 预约送到时间
     */
    private Integer serviceTime;

    /**
     * 接单时间
     */
    private Integer orderTime;

    /**
     * 完成时间
     */
    private Integer finishTime;

    /**
     * 
     */
    private Integer ctime;

    /**
     * 
     */
    private Integer integral;

    /**
     * 0不预约1预约
     */
    private Short isAppointment;

    /**
     * 预约结束时间
     */
    private Integer endTime;

    /**
     * 记录原价
     */
    private BigDecimal originalPrice;

    /**
     * 第三方支付平台订单号
     */
    private String outTradeNo;

    /**
     * 优惠券金额
     */
    private BigDecimal couponPrice;

    /**
     * 菜场优惠金额
     */
    private BigDecimal marketActivityPrice;

    /**
     * 商户活动减免
     */
    private BigDecimal storeActivityPrice;

    /**
     * VIP减免
     */
    private BigDecimal vipRelief;

    /**
     * 错误金额
     */
    private BigDecimal errorPrice;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 测试订单标识
     */
    private Integer test;

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
     * 配送订单
     * @return rider_sn 配送订单
     */
    public String getRiderSn() {
        return riderSn;
    }

    /**
     * 配送订单
     * @param riderSn 配送订单
     */
    public void setRiderSn(String riderSn) {
        this.riderSn = riderSn == null ? null : riderSn.trim();
    }

    /**
     * 配送的所有订单
     * @return order_sn 配送的所有订单
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * 配送的所有订单
     * @param orderSn 配送的所有订单
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn == null ? null : orderSn.trim();
    }

    /**
     * 待接单、1配送中、2已送达、3自提、4已评价
     * @return rider_status 待接单、1配送中、2已送达、3自提、4已评价
     */
    public Byte getRiderStatus() {
        return riderStatus;
    }

    /**
     * 待接单、1配送中、2已送达、3自提、4已评价
     * @param riderStatus 待接单、1配送中、2已送达、3自提、4已评价
     */
    public void setRiderStatus(Byte riderStatus) {
        this.riderStatus = riderStatus;
    }

    /**
     * 待支付、1支付成功、2支付失败
     * @return pay_status 待支付、1支付成功、2支付失败
     */
    public Byte getPayStatus() {
        return payStatus;
    }

    /**
     * 待支付、1支付成功、2支付失败
     * @param payStatus 待支付、1支付成功、2支付失败
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
     * 配送人员
     * @return rid 配送人员
     */
    public Integer getRid() {
        return rid;
    }

    /**
     * 配送人员
     * @param rid 配送人员
     */
    public void setRid(Integer rid) {
        this.rid = rid;
    }

    /**
     * 菜场id
     * @return marketid 菜场id
     */
    public Integer getMarketid() {
        return marketid;
    }

    /**
     * 菜场id
     * @param marketid 菜场id
     */
    public void setMarketid(Integer marketid) {
        this.marketid = marketid;
    }

    /**
     * 配送费用
     * @return rider_pay 配送费用
     */
    public BigDecimal getRiderPay() {
        return riderPay;
    }

    /**
     * 配送费用
     * @param riderPay 配送费用
     */
    public void setRiderPay(BigDecimal riderPay) {
        this.riderPay = riderPay;
    }

    /**
     * 配送地址
     * @return address_id 配送地址
     */
    public Integer getAddressId() {
        return addressId;
    }

    /**
     * 配送地址
     * @param addressId 配送地址
     */
    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    /**
     * 用户优惠券id
     * @return couponid 用户优惠券id
     */
    public Integer getCouponid() {
        return couponid;
    }

    /**
     * 用户优惠券id
     * @param couponid 用户优惠券id
     */
    public void setCouponid(Integer couponid) {
        this.couponid = couponid;
    }

    /**
     * 支付方式
     * @return type_pay 支付方式
     */
    public String getTypePay() {
        return typePay;
    }

    /**
     * 支付方式
     * @param typePay 支付方式
     */
    public void setTypePay(String typePay) {
        this.typePay = typePay == null ? null : typePay.trim();
    }

    /**
     * 
     * @return totalPrice 
     */
    public Double getTotalprice() {
        return totalprice;
    }

    /**
     * 
     * @param totalprice 
     */
    public void setTotalprice(Double totalprice) {
        this.totalprice = totalprice;
    }

    /**
     * 订单支付时间
     * @return pay_time 订单支付时间
     */
    public Integer getPayTime() {
        return payTime;
    }

    /**
     * 订单支付时间
     * @param payTime 订单支付时间
     */
    public void setPayTime(Integer payTime) {
        this.payTime = payTime;
    }

    /**
     * 预约送到时间
     * @return service_time 预约送到时间
     */
    public Integer getServiceTime() {
        return serviceTime;
    }

    /**
     * 预约送到时间
     * @param serviceTime 预约送到时间
     */
    public void setServiceTime(Integer serviceTime) {
        this.serviceTime = serviceTime;
    }

    /**
     * 接单时间
     * @return order_time 接单时间
     */
    public Integer getOrderTime() {
        return orderTime;
    }

    /**
     * 接单时间
     * @param orderTime 接单时间
     */
    public void setOrderTime(Integer orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * 完成时间
     * @return finish_time 完成时间
     */
    public Integer getFinishTime() {
        return finishTime;
    }

    /**
     * 完成时间
     * @param finishTime 完成时间
     */
    public void setFinishTime(Integer finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * 
     * @return ctime 
     */
    public Integer getCtime() {
        return ctime;
    }

    /**
     * 
     * @param ctime 
     */
    public void setCtime(Integer ctime) {
        this.ctime = ctime;
    }

    /**
     * 
     * @return integral 
     */
    public Integer getIntegral() {
        return integral;
    }

    /**
     * 
     * @param integral 
     */
    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    /**
     * 0不预约1预约
     * @return is_appointment 0不预约1预约
     */
    public Short getIsAppointment() {
        return isAppointment;
    }

    /**
     * 0不预约1预约
     * @param isAppointment 0不预约1预约
     */
    public void setIsAppointment(Short isAppointment) {
        this.isAppointment = isAppointment;
    }

    /**
     * 预约结束时间
     * @return end_time 预约结束时间
     */
    public Integer getEndTime() {
        return endTime;
    }

    /**
     * 预约结束时间
     * @param endTime 预约结束时间
     */
    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    /**
     * 记录原价
     * @return original_price 记录原价
     */
    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    /**
     * 记录原价
     * @param originalPrice 记录原价
     */
    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    /**
     * 第三方支付平台订单号
     * @return out_trade_no 第三方支付平台订单号
     */
    public String getOutTradeNo() {
        return outTradeNo;
    }

    /**
     * 第三方支付平台订单号
     * @param outTradeNo 第三方支付平台订单号
     */
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    /**
     * 优惠券金额
     * @return coupon_price 优惠券金额
     */
    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    /**
     * 优惠券金额
     * @param couponPrice 优惠券金额
     */
    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

    /**
     * 菜场优惠金额
     * @return market_activity_price 菜场优惠金额
     */
    public BigDecimal getMarketActivityPrice() {
        return marketActivityPrice;
    }

    /**
     * 菜场优惠金额
     * @param marketActivityPrice 菜场优惠金额
     */
    public void setMarketActivityPrice(BigDecimal marketActivityPrice) {
        this.marketActivityPrice = marketActivityPrice;
    }

    /**
     * 商户活动减免
     * @return store_activity_price 商户活动减免
     */
    public BigDecimal getStoreActivityPrice() {
        return storeActivityPrice;
    }

    /**
     * 商户活动减免
     * @param storeActivityPrice 商户活动减免
     */
    public void setStoreActivityPrice(BigDecimal storeActivityPrice) {
        this.storeActivityPrice = storeActivityPrice;
    }

    /**
     * VIP减免
     * @return vip_relief VIP减免
     */
    public BigDecimal getVipRelief() {
        return vipRelief;
    }

    /**
     * VIP减免
     * @param vipRelief VIP减免
     */
    public void setVipRelief(BigDecimal vipRelief) {
        this.vipRelief = vipRelief;
    }

    /**
     * 错误金额
     * @return error_price 错误金额
     */
    public BigDecimal getErrorPrice() {
        return errorPrice;
    }

    /**
     * 错误金额
     * @param errorPrice 错误金额
     */
    public void setErrorPrice(BigDecimal errorPrice) {
        this.errorPrice = errorPrice;
    }

    /**
     * 备注信息
     * @return remark 备注信息
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注信息
     * @param remark 备注信息
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 测试订单标识
     * @return test 测试订单标识
     */
    public Integer getTest() {
        return test;
    }

    /**
     * 测试订单标识
     * @param test 测试订单标识
     */
    public void setTest(Integer test) {
        this.test = test;
    }
}