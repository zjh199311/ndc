package com.zhongjian.dao.entity.order.coupon;

public class OrderCouponBean {
    /**
     * 
     */
    private Integer id;

    /**
     * 优惠券类型0满减券1配送券2全场通用
     */
    private Byte type;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 0所有用户1新用户2下单用户
     */
    private Byte useObject;

    /**
     * 0用户手动领取1扫码领取2自动发放3西湖文化专属
     */
    private Byte grant;

    /**
     * 每人限领数量
     */
    private Integer number;

    /**
     * 总投放量
     */
    private Integer totalNums;

    /**
     * 0全场通用券1按店铺2按品类3按商品
     */
    private Byte useScope;

    /**
     * 
     */
    private Long payFull;

    /**
     * 优惠金额
     */
    private Long coupon;

    /**
     * 开始时间
     */
    private Integer startTime;

    /**
     * 结束时间
     */
    private Integer endTime;

    /**
     * 优惠券描述
     */
    private String desc;

    /**
     * 0未过期1已过期
     */
    private Byte timeout;

    /**
     * 创建时间
     */
    private Integer ctime;

    /**
     * 
     */
    private String pic;

    /**
     * 
     */
    private String marketid;

    /**
     * 
     */
    private Integer pid;

    /**
     *  到期类型1固定时间2按小时
     */
    private Integer effectType;

    /**
     * 到期时间/小时数
     */
    private Integer effectTime;

    /**
     * 
     */
    private String mongoid;

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
     * 优惠券类型0满减券1配送券2全场通用
     * @return type 优惠券类型0满减券1配送券2全场通用
     */
    public Byte getType() {
        return type;
    }

    /**
     * 优惠券类型0满减券1配送券2全场通用
     * @param type 优惠券类型0满减券1配送券2全场通用
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 优惠券名称
     * @return name 优惠券名称
     */
    public String getName() {
        return name;
    }

    /**
     * 优惠券名称
     * @param name 优惠券名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 0所有用户1新用户2下单用户
     * @return use_object 0所有用户1新用户2下单用户
     */
    public Byte getUseObject() {
        return useObject;
    }

    /**
     * 0所有用户1新用户2下单用户
     * @param useObject 0所有用户1新用户2下单用户
     */
    public void setUseObject(Byte useObject) {
        this.useObject = useObject;
    }

    /**
     * 0用户手动领取1扫码领取2自动发放3西湖文化专属
     * @return grant 0用户手动领取1扫码领取2自动发放3西湖文化专属
     */
    public Byte getGrant() {
        return grant;
    }

    /**
     * 0用户手动领取1扫码领取2自动发放3西湖文化专属
     * @param grant 0用户手动领取1扫码领取2自动发放3西湖文化专属
     */
    public void setGrant(Byte grant) {
        this.grant = grant;
    }

    /**
     * 每人限领数量
     * @return number 每人限领数量
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * 每人限领数量
     * @param number 每人限领数量
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 总投放量
     * @return total_nums 总投放量
     */
    public Integer getTotalNums() {
        return totalNums;
    }

    /**
     * 总投放量
     * @param totalNums 总投放量
     */
    public void setTotalNums(Integer totalNums) {
        this.totalNums = totalNums;
    }

    /**
     * 0全场通用券1按店铺2按品类3按商品
     * @return use_scope 0全场通用券1按店铺2按品类3按商品
     */
    public Byte getUseScope() {
        return useScope;
    }

    /**
     * 0全场通用券1按店铺2按品类3按商品
     * @param useScope 0全场通用券1按店铺2按品类3按商品
     */
    public void setUseScope(Byte useScope) {
        this.useScope = useScope;
    }

    /**
     * 
     * @return pay_full 
     */
    public Long getPayFull() {
        return payFull;
    }

    /**
     * 
     * @param payFull 
     */
    public void setPayFull(Long payFull) {
        this.payFull = payFull;
    }

    /**
     * 优惠金额
     * @return coupon 优惠金额
     */
    public Long getCoupon() {
        return coupon;
    }

    /**
     * 优惠金额
     * @param coupon 优惠金额
     */
    public void setCoupon(Long coupon) {
        this.coupon = coupon;
    }

    /**
     * 开始时间
     * @return start_time 开始时间
     */
    public Integer getStartTime() {
        return startTime;
    }

    /**
     * 开始时间
     * @param startTime 开始时间
     */
    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    /**
     * 结束时间
     * @return end_time 结束时间
     */
    public Integer getEndTime() {
        return endTime;
    }

    /**
     * 结束时间
     * @param endTime 结束时间
     */
    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    /**
     * 优惠券描述
     * @return desc 优惠券描述
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 优惠券描述
     * @param desc 优惠券描述
     */
    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    /**
     * 0未过期1已过期
     * @return timeout 0未过期1已过期
     */
    public Byte getTimeout() {
        return timeout;
    }

    /**
     * 0未过期1已过期
     * @param timeout 0未过期1已过期
     */
    public void setTimeout(Byte timeout) {
        this.timeout = timeout;
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
     * 
     * @return pic 
     */
    public String getPic() {
        return pic;
    }

    /**
     * 
     * @param pic 
     */
    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    /**
     * 
     * @return marketid 
     */
    public String getMarketid() {
        return marketid;
    }

    /**
     * 
     * @param marketid 
     */
    public void setMarketid(String marketid) {
        this.marketid = marketid == null ? null : marketid.trim();
    }

    /**
     * 
     * @return pid 
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * 
     * @param pid 
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     *  到期类型1固定时间2按小时
     * @return effect_type  到期类型1固定时间2按小时
     */
    public Integer getEffectType() {
        return effectType;
    }

    /**
     *  到期类型1固定时间2按小时
     * @param effectType  到期类型1固定时间2按小时
     */
    public void setEffectType(Integer effectType) {
        this.effectType = effectType;
    }

    /**
     * 到期时间/小时数
     * @return effect_time 到期时间/小时数
     */
    public Integer getEffectTime() {
        return effectTime;
    }

    /**
     * 到期时间/小时数
     * @param effectTime 到期时间/小时数
     */
    public void setEffectTime(Integer effectTime) {
        this.effectTime = effectTime;
    }

    /**
     * 
     * @return mongoid 
     */
    public String getMongoid() {
        return mongoid;
    }

    /**
     * 
     * @param mongoid 
     */
    public void setMongoid(String mongoid) {
        this.mongoid = mongoid == null ? null : mongoid.trim();
    }
}