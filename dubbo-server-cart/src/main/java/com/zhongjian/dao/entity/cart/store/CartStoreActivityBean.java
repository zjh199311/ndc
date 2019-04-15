package com.zhongjian.dao.entity.cart.store;

import java.math.BigDecimal;

public class CartStoreActivityBean {
    /**
     * 
     */
    private Integer id;

    /**
     * 商户id
     */
    private Integer sid;

    /**
     * 满减额度
     */
    private BigDecimal full;

    /**
     * 优惠数值
     */
    private BigDecimal reduce;

    /**
     * 折扣
     */
    private String discount;

    /**
     * 类型0满减1折扣
     */
    private Byte type;

    /**
     * 0关闭1开启
     */
    private Byte enable;

    /**
     * 0存在1删除
     */
    private Byte isDelete;

    /**
     * 
     */
    private Integer examine;

    /**
     * 
     */
    private String examineInfo;

    /**
     * 
     */
    private Integer ctime;

    /**
     * 
     */
    private Integer applyTime;

    /**
     * 
     */
    private Integer examineTime;

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
     * 满减额度
     * @return full 满减额度
     */
    public BigDecimal getFull() {
        return full;
    }

    /**
     * 满减额度
     * @param full 满减额度
     */
    public void setFull(BigDecimal full) {
        this.full = full;
    }

    /**
     * 优惠数值
     * @return reduce 优惠数值
     */
    public BigDecimal getReduce() {
        return reduce;
    }

    /**
     * 优惠数值
     * @param reduce 优惠数值
     */
    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }

    /**
     * 折扣
     * @return discount 折扣
     */
    public String getDiscount() {
        return discount;
    }

    /**
     * 折扣
     * @param discount 折扣
     */
    public void setDiscount(String discount) {
        this.discount = discount == null ? null : discount.trim();
    }

    /**
     * 类型0满减1折扣
     * @return type 类型0满减1折扣
     */
    public Byte getType() {
        return type;
    }

    /**
     * 类型0满减1折扣
     * @param type 类型0满减1折扣
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 0关闭1开启
     * @return enable 0关闭1开启
     */
    public Byte getEnable() {
        return enable;
    }

    /**
     * 0关闭1开启
     * @param enable 0关闭1开启
     */
    public void setEnable(Byte enable) {
        this.enable = enable;
    }

    /**
     * 0存在1删除
     * @return is_delete 0存在1删除
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * 0存在1删除
     * @param isDelete 0存在1删除
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 
     * @return examine 
     */
    public Integer getExamine() {
        return examine;
    }

    /**
     * 
     * @param examine 
     */
    public void setExamine(Integer examine) {
        this.examine = examine;
    }

    /**
     * 
     * @return examine_info 
     */
    public String getExamineInfo() {
        return examineInfo;
    }

    /**
     * 
     * @param examineInfo 
     */
    public void setExamineInfo(String examineInfo) {
        this.examineInfo = examineInfo == null ? null : examineInfo.trim();
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
     * @return apply_time 
     */
    public Integer getApplyTime() {
        return applyTime;
    }

    /**
     * 
     * @param applyTime 
     */
    public void setApplyTime(Integer applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * 
     * @return examine_time 
     */
    public Integer getExamineTime() {
        return examineTime;
    }

    /**
     * 
     * @param examineTime 
     */
    public void setExamineTime(Integer examineTime) {
        this.examineTime = examineTime;
    }
}