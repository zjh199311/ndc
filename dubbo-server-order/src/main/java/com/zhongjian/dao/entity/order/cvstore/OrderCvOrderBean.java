package com.zhongjian.dao.entity.order.cvstore;

import java.math.BigDecimal;

public class OrderCvOrderBean {
    /**
     * 
     */
    private Integer id;

    /**
     * 订单序列号
     */
    private String orderSn;

    /**
     * 商户id
     */
    private Integer sid;

    /**
     * 原价(商品原价)
     */
    private BigDecimal total;

    /**
     * 现价(用户最终该得的金额)
     */
    private BigDecimal payment;

    /**
     * 创建时间
     */
    private Integer ctime;

    /**
     * 接单时间(商户点击接单的时间)
     */
    private Integer ordertakingTime;

    /**
     * 订单完成时间(商户点击完单的时间)
     */
    private Integer orderendTime;

    /**
     * 地址id(商户送单地址及用户相关信息)
     */
    private Integer addressid;

    /**
     * 0待接单1接单2送达3自提4评价完成
     */
    private Byte orderStatus;

    /**
     * 0待支付1支付（冗余用户订单的支付状态）
     */
    private Byte payStatus;

    /**
     * 配送费(商户收取的手续费)
     */
    private BigDecimal deliverFee;

    /**
     * 备注
     */
    private String remark;

    /**
     * 关联的用户订单
     */
    private Integer uoid;

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
     * 订单序列号
     * @return order_sn 订单序列号
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * 订单序列号
     * @param orderSn 订单序列号
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn == null ? null : orderSn.trim();
    }

    /**
     * 商户id
     * @return sid 商户id
     */
    public Integer getSid() {
        return sid;
    }

    /**
     * 商户id
     * @param sid 商户id
     */
    public void setSid(Integer sid) {
        this.sid = sid;
    }

    /**
     * 原价(商品原价)
     * @return total 原价(商品原价)
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * 原价(商品原价)
     * @param total 原价(商品原价)
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * 现价(用户最终该得的金额)
     * @return payment 现价(用户最终该得的金额)
     */
    public BigDecimal getPayment() {
        return payment;
    }

    /**
     * 现价(用户最终该得的金额)
     * @param payment 现价(用户最终该得的金额)
     */
    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    /**
     * 创建时间
     * @return ctime 创建时间
     */
    public Integer getCtime() {
        return ctime;
    }

    /**
     * 创建时间
     * @param ctime 创建时间
     */
    public void setCtime(Integer ctime) {
        this.ctime = ctime;
    }

    /**
     * 接单时间(商户点击接单的时间)
     * @return ordertaking_time 接单时间(商户点击接单的时间)
     */
    public Integer getOrdertakingTime() {
        return ordertakingTime;
    }

    /**
     * 接单时间(商户点击接单的时间)
     * @param ordertakingTime 接单时间(商户点击接单的时间)
     */
    public void setOrdertakingTime(Integer ordertakingTime) {
        this.ordertakingTime = ordertakingTime;
    }

    /**
     * 订单完成时间(商户点击完单的时间)
     * @return orderend_time 订单完成时间(商户点击完单的时间)
     */
    public Integer getOrderendTime() {
        return orderendTime;
    }

    /**
     * 订单完成时间(商户点击完单的时间)
     * @param orderendTime 订单完成时间(商户点击完单的时间)
     */
    public void setOrderendTime(Integer orderendTime) {
        this.orderendTime = orderendTime;
    }

    /**
     * 地址id(商户送单地址及用户相关信息)
     * @return addressid 地址id(商户送单地址及用户相关信息)
     */
    public Integer getAddressid() {
        return addressid;
    }

    /**
     * 地址id(商户送单地址及用户相关信息)
     * @param addressid 地址id(商户送单地址及用户相关信息)
     */
    public void setAddressid(Integer addressid) {
        this.addressid = addressid;
    }

    /**
     * 0待接单1接单2送达3自提4评价完成
     * @return order_status 0待接单1接单2送达3自提4评价完成
     */
    public Byte getOrderStatus() {
        return orderStatus;
    }

    /**
     * 0待接单1接单2送达3自提4评价完成
     * @param orderStatus 0待接单1接单2送达3自提4评价完成
     */
    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * 0待支付1支付（冗余用户订单的支付状态）
     * @return pay_status 0待支付1支付（冗余用户订单的支付状态）
     */
    public Byte getPayStatus() {
        return payStatus;
    }

    /**
     * 0待支付1支付（冗余用户订单的支付状态）
     * @param payStatus 0待支付1支付（冗余用户订单的支付状态）
     */
    public void setPayStatus(Byte payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * 配送费(商户收取的手续费)
     * @return deliver_fee 配送费(商户收取的手续费)
     */
    public BigDecimal getDeliverFee() {
        return deliverFee;
    }

    /**
     * 配送费(商户收取的手续费)
     * @param deliverFee 配送费(商户收取的手续费)
     */
    public void setDeliverFee(BigDecimal deliverFee) {
        this.deliverFee = deliverFee;
    }

    /**
     * 备注
     * @return remark 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 关联的用户订单
     * @return uoid 关联的用户订单
     */
    public Integer getUoid() {
        return uoid;
    }

    /**
     * 关联的用户订单
     * @param uoid 关联的用户订单
     */
    public void setUoid(Integer uoid) {
        this.uoid = uoid;
    }
}