package com.zhongjian.dao.entity.order.cart;

import java.math.BigDecimal;

public class OrderCartBean {
    /**
     * 
     */
    private Integer cid;

    /**
     * 菜品id
     */
    private Integer gid;

    /**
     * 
     */
    private String gname;

    /**
     * 商户id
     */
    private Integer pid;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 
     */
    private String unit;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 斤两
     */
    private BigDecimal amount;

    /**
     * 
     */
    private Integer oid;

    /**
     * 
     */
    private Integer sid;

    /**
     * 
     */
    private Integer status;

    /**
     * 
     */
    private String ctime;

    /**
     * 
     */
    private String remark;

    /**
     * 
     * @return cid 
     */
    public Integer getCid() {
        return cid;
    }

    /**
     * 
     * @param cid 
     */
    public void setCid(Integer cid) {
        this.cid = cid;
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
     * 
     * @return gname 
     */
    public String getGname() {
        return gname;
    }

    /**
     * 
     * @param gname 
     */
    public void setGname(String gname) {
        this.gname = gname == null ? null : gname.trim();
    }

    /**
     * 商户id
     * @return pid 商户id
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * 商户id
     * @param pid 商户id
     */
    public void setPid(Integer pid) {
        this.pid = pid;
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
     * 
     * @return unit 
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 
     * @param unit 
     */
    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
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
     * 
     * @return oid 
     */
    public Integer getOid() {
        return oid;
    }

    /**
     * 
     * @param oid 
     */
    public void setOid(Integer oid) {
        this.oid = oid;
    }

    /**
     * 
     * @return sid 
     */
    public Integer getSid() {
        return sid;
    }

    /**
     * 
     * @param sid 
     */
    public void setSid(Integer sid) {
        this.sid = sid;
    }

    /**
     * 
     * @return status 
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 
     * @param status 
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 
     * @return ctime 
     */
    public String getCtime() {
        return ctime;
    }

    /**
     * 
     * @param ctime 
     */
    public void setCtime(String ctime) {
        this.ctime = ctime == null ? null : ctime.trim();
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