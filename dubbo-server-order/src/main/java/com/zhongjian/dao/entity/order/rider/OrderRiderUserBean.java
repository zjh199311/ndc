package com.zhongjian.dao.entity.order.rider;

import java.math.BigDecimal;

public class OrderRiderUserBean {
    /**
     * 骑手名称
     */
    private Integer rid;

    /**
     * 所属菜场
     */
    private Integer marketid;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 
     */
    private String name;

    /**
     * 身份证号
     */
    private String code;

    /**
     * 密码
     */
    private String password;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 0待审核1审核通过
     */
    private Byte state;

    /**
     * 
     */
    private Integer ctime;

    /**
     * 加密盐
     */
    private String salt;

    /**
     * 1正常0禁用
     */
    private Byte status;

    /**
     * 0结束接单1接单
     */
    private Byte isOrder;

    /**
     * 
     */
    private String deviceNumber;

    /**
     * accid
     */
    private String accid;

    /**
     * 
     */
    private String token;

    /**
     * 
     */
    private Integer testUser;

    /**
     * 登录凭证
     */
    private String loginToken;

    /**
     * 累计VIP推广奖励
     */
    private BigDecimal vipPromotionReward;

    /**
     * 已提现VIP推广奖励
     */
    private BigDecimal vipPromotionRewardget;

    /**
     * 未提现VIP推广奖励
     */
    private BigDecimal vipPromotionRewardover;

    /**
     * 公众号openid
     */
    private String publicOpenid;

    /**
     * 骑手名称
     * @return rid 骑手名称
     */
    public Integer getRid() {
        return rid;
    }

    /**
     * 骑手名称
     * @param rid 骑手名称
     */
    public void setRid(Integer rid) {
        this.rid = rid;
    }

    /**
     * 所属菜场
     * @return marketid 所属菜场
     */
    public Integer getMarketid() {
        return marketid;
    }

    /**
     * 所属菜场
     * @param marketid 所属菜场
     */
    public void setMarketid(Integer marketid) {
        this.marketid = marketid;
    }

    /**
     * 手机号
     * @return phone 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 手机号
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 
     * @return name 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 身份证号
     * @return code 身份证号
     */
    public String getCode() {
        return code;
    }

    /**
     * 身份证号
     * @param code 身份证号
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 密码
     * @return password 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 密码
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 经度
     * @return longitude 经度
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 经度
     * @param longitude 经度
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    /**
     * 纬度
     * @return latitude 纬度
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 纬度
     * @param latitude 纬度
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    /**
     * 0待审核1审核通过
     * @return state 0待审核1审核通过
     */
    public Byte getState() {
        return state;
    }

    /**
     * 0待审核1审核通过
     * @param state 0待审核1审核通过
     */
    public void setState(Byte state) {
        this.state = state;
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
     * 加密盐
     * @return salt 加密盐
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 加密盐
     * @param salt 加密盐
     */
    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    /**
     * 1正常0禁用
     * @return status 1正常0禁用
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 1正常0禁用
     * @param status 1正常0禁用
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 0结束接单1接单
     * @return is_order 0结束接单1接单
     */
    public Byte getIsOrder() {
        return isOrder;
    }

    /**
     * 0结束接单1接单
     * @param isOrder 0结束接单1接单
     */
    public void setIsOrder(Byte isOrder) {
        this.isOrder = isOrder;
    }

    /**
     * 
     * @return device_number 
     */
    public String getDeviceNumber() {
        return deviceNumber;
    }

    /**
     * 
     * @param deviceNumber 
     */
    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber == null ? null : deviceNumber.trim();
    }

    /**
     * accid
     * @return accid accid
     */
    public String getAccid() {
        return accid;
    }

    /**
     * accid
     * @param accid accid
     */
    public void setAccid(String accid) {
        this.accid = accid == null ? null : accid.trim();
    }

    /**
     * 
     * @return token 
     */
    public String getToken() {
        return token;
    }

    /**
     * 
     * @param token 
     */
    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    /**
     * 
     * @return test_user 
     */
    public Integer getTestUser() {
        return testUser;
    }

    /**
     * 
     * @param testUser 
     */
    public void setTestUser(Integer testUser) {
        this.testUser = testUser;
    }

    /**
     * 登录凭证
     * @return login_token 登录凭证
     */
    public String getLoginToken() {
        return loginToken;
    }

    /**
     * 登录凭证
     * @param loginToken 登录凭证
     */
    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken == null ? null : loginToken.trim();
    }

    /**
     * 累计VIP推广奖励
     * @return vip_promotion_reward 累计VIP推广奖励
     */
    public BigDecimal getVipPromotionReward() {
        return vipPromotionReward;
    }

    /**
     * 累计VIP推广奖励
     * @param vipPromotionReward 累计VIP推广奖励
     */
    public void setVipPromotionReward(BigDecimal vipPromotionReward) {
        this.vipPromotionReward = vipPromotionReward;
    }

    /**
     * 已提现VIP推广奖励
     * @return vip_promotion_rewardget 已提现VIP推广奖励
     */
    public BigDecimal getVipPromotionRewardget() {
        return vipPromotionRewardget;
    }

    /**
     * 已提现VIP推广奖励
     * @param vipPromotionRewardget 已提现VIP推广奖励
     */
    public void setVipPromotionRewardget(BigDecimal vipPromotionRewardget) {
        this.vipPromotionRewardget = vipPromotionRewardget;
    }

    /**
     * 未提现VIP推广奖励
     * @return vip_promotion_rewardover 未提现VIP推广奖励
     */
    public BigDecimal getVipPromotionRewardover() {
        return vipPromotionRewardover;
    }

    /**
     * 未提现VIP推广奖励
     * @param vipPromotionRewardover 未提现VIP推广奖励
     */
    public void setVipPromotionRewardover(BigDecimal vipPromotionRewardover) {
        this.vipPromotionRewardover = vipPromotionRewardover;
    }

    /**
     * 公众号openid
     * @return public_openid 公众号openid
     */
    public String getPublicOpenid() {
        return publicOpenid;
    }

    /**
     * 公众号openid
     * @param publicOpenid 公众号openid
     */
    public void setPublicOpenid(String publicOpenid) {
        this.publicOpenid = publicOpenid == null ? null : publicOpenid.trim();
    }
}