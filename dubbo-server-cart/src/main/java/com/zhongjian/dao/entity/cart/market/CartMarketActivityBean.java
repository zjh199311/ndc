package com.zhongjian.dao.entity.cart.market;

public class CartMarketActivityBean {
    /**
     * 菜场id
     */
    private Integer marketid;

    /**
     * 活动类型0满减1折扣
     */
    private Byte type;

    /**
     * 活动描述
     */
    private String rule;

    /**
     * 0关闭1打开
     */
    private Byte status;

    /**
     * 
     */
    private Integer upLimit;

    /**
     * 
     */
    private Integer belongType;

    /**
     * 
     */
    private String ename;

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
     * 活动类型0满减1折扣
     * @return type 活动类型0满减1折扣
     */
    public Byte getType() {
        return type;
    }

    /**
     * 活动类型0满减1折扣
     * @param type 活动类型0满减1折扣
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 活动描述
     * @return rule 活动描述
     */
    public String getRule() {
        return rule;
    }

    /**
     * 活动描述
     * @param rule 活动描述
     */
    public void setRule(String rule) {
        this.rule = rule == null ? null : rule.trim();
    }

    /**
     * 0关闭1打开
     * @return status 0关闭1打开
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 0关闭1打开
     * @param status 0关闭1打开
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 
     * @return up_limit 
     */
    public Integer getUpLimit() {
        return upLimit;
    }

    /**
     * 
     * @param upLimit 
     */
    public void setUpLimit(Integer upLimit) {
        this.upLimit = upLimit;
    }

    /**
     * 
     * @return belong_type 
     */
    public Integer getBelongType() {
        return belongType;
    }

    /**
     * 
     * @param belongType 
     */
    public void setBelongType(Integer belongType) {
        this.belongType = belongType;
    }

    /**
     * 
     * @return ename 
     */
    public String getEname() {
        return ename;
    }

    /**
     * 
     * @param ename 
     */
    public void setEname(String ename) {
        this.ename = ename == null ? null : ename.trim();
    }
}