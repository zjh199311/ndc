package com.zhongjian.dao.entity.cart.basket;

import java.math.BigDecimal;

public class CartBasketBean {
    /**
     * 
     */
    private Integer id;

    /**
     * 菜品id
     */
    private Integer gid;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 单价
     */
    private BigDecimal unitprice;

    /**
     * 斤两
     */
    private BigDecimal amount;

    /**
     * 商户id
     */
    private Integer sid;

    /**
     * 
     */
    private Integer ctime;

    /**
     * 
     */
    private String remark;

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
     * 菜品id
     * @return gid 菜品id
     */
    public Integer getGid() {
        return gid;
    }

    /**
     * 菜品id
     * @param gid 菜品id
     */
    public void setGid(Integer gid) {
        this.gid = gid;
    }

    /**
     * 用户id
     * @return uid 用户id
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * 用户id
     * @param uid 用户id
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * 价格
     * @return price 价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 价格
     * @param price 价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 单价
     * @return unitPrice 单价
     */
    public BigDecimal getUnitprice() {
        return unitprice;
    }

    /**
     * 单价
     * @param unitprice 单价
     */
    public void setUnitprice(BigDecimal unitprice) {
        this.unitprice = unitprice;
    }

    /**
     * 斤两
     * @return amount 斤两
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 斤两
     * @param amount 斤两
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
     * @return remark 
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 
     * @param remark 
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}