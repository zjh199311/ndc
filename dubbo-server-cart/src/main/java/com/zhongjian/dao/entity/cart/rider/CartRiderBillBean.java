package com.zhongjian.dao.entity.cart.rider;

public class CartRiderBillBean {
    /**
     * 
     */
    private Integer id;

    /**
     * 骑手id
     */
    private Integer rid;

    /**
     * 骑手订单ID
     */
    private Integer roid;

    /**
     * 骑手订单ID
     */
    private Integer oid;

    /**
     * 骑手单号
     */
    private String riderSn;

    /**
     * 收入
     */
    private Long income;

    /**
     * 创建时间
     */
    private Integer ctime;

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
     * 骑手id
     * @return rid 骑手id
     */
    public Integer getRid() {
        return rid;
    }

    /**
     * 骑手id
     * @param rid 骑手id
     */
    public void setRid(Integer rid) {
        this.rid = rid;
    }

    /**
     * 骑手订单ID
     * @return roid 骑手订单ID
     */
    public Integer getRoid() {
        return roid;
    }

    /**
     * 骑手订单ID
     * @param roid 骑手订单ID
     */
    public void setRoid(Integer roid) {
        this.roid = roid;
    }

    /**
     * 骑手订单ID
     * @return oid 骑手订单ID
     */
    public Integer getOid() {
        return oid;
    }

    /**
     * 骑手订单ID
     * @param oid 骑手订单ID
     */
    public void setOid(Integer oid) {
        this.oid = oid;
    }

    /**
     * 骑手单号
     * @return rider_sn 骑手单号
     */
    public String getRiderSn() {
        return riderSn;
    }

    /**
     * 骑手单号
     * @param riderSn 骑手单号
     */
    public void setRiderSn(String riderSn) {
        this.riderSn = riderSn == null ? null : riderSn.trim();
    }

    /**
     * 收入
     * @return income 收入
     */
    public Long getIncome() {
        return income;
    }

    /**
     * 收入
     * @param income 收入
     */
    public void setIncome(Long income) {
        this.income = income;
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
}