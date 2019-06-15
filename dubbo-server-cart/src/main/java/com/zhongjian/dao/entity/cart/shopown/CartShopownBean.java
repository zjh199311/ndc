package com.zhongjian.dao.entity.cart.shopown;

import java.math.BigDecimal;

public class CartShopownBean {
    /**
     * 商户id
     */
    private Integer pid;

    /**
     * 商户图片
     */
    private String pic;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 商户名称
     */
    private String sname;

    /**
     * 真实名称
     */
    private String realName;

    /**
     * 店铺经营范围
     */
    private String scopeBusiness;

    /**
     * 工商营业执照
     */
    private String businessLicense;

    /**
     * 食品许可证
     */
    private String foodLicense;

    /**
     * 身份证图片
     */
    private String cardPic;

    /**
     * 商户营业额
     */
    private BigDecimal money;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 所在菜场id
     */
    private Integer marketid;

    /**
     * 是否推荐1未推荐
     */
    private Byte isHot;

    /**
     * 是否推荐1推荐
     */
    private Byte isRecommend;

    /**
     * 等级
     */
    private String grade;

    /**
     * 摊位门牌号
     */
    private String address;

    /**
     * 网易账号
     */
    private String accid;

    /**
     * 网易token
     */
    private String token;

    /**
     * 手机设备号
     */
    private String deviceNumber;

    /**
     * 摄像头id
     */
    private String cameraid;

    /**
     * 房间号
     */
    private String roomid;

    /**
     * 2预约中1打样0开张
     */
    private Integer status;

    /**
     * 
     */
    private Integer ctime;

    /**
     * 加密盐
     */
    private String salt;

    /**
     * 1为菜场自提点
     */
    private Boolean isSince;

    /**
     * 打印机编号
     */
    private String printerNum;

    /**
     * 打印机key
     */
    private String printerKey;

    /**
     * 0审核中1审核通过2审核失败
     */
    private Integer examine;

    /**
     * 0不显示1显示
     */
    private Integer isShow;

    /**
     * 1正式商户0测试商户
     */
    private Byte type;

    /**
     * 0关闭预约1开启预约
     */
    private Integer isAppointment;

    /**
     * 推荐值，应急
     */
    private Integer recommend;

    /**
     * 审核反馈信息
     */
    private String examineInfo;

    /**
     * 1小米2华为
     */
    private String phonetype;

    /**
     * 推送token
     */
    private String pushtoken;

    /**
     * 0优惠1不优惠
     */
    private Integer unfavorable;

    /**
     * 网易云信Token
     */
    private String loginToken;

    /**
     * 登录凭证
     */
    private String yxToken;

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
     * 商铺模式：0菜场商户1便利店商户
     */
    private Byte mode;

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
     * 商户图片
     * @return pic 商户图片
     */
    public String getPic() {
        return pic;
    }

    /**
     * 商户图片
     * @param pic 商户图片
     */
    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    /**
     * 联系电话
     * @return phone 联系电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 联系电话
     * @param phone 联系电话
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
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
     * 商户名称
     * @return sname 商户名称
     */
    public String getSname() {
        return sname;
    }

    /**
     * 商户名称
     * @param sname 商户名称
     */
    public void setSname(String sname) {
        this.sname = sname == null ? null : sname.trim();
    }

    /**
     * 真实名称
     * @return real_name 真实名称
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 真实名称
     * @param realName 真实名称
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * 店铺经营范围
     * @return scope_business 店铺经营范围
     */
    public String getScopeBusiness() {
        return scopeBusiness;
    }

    /**
     * 店铺经营范围
     * @param scopeBusiness 店铺经营范围
     */
    public void setScopeBusiness(String scopeBusiness) {
        this.scopeBusiness = scopeBusiness == null ? null : scopeBusiness.trim();
    }

    /**
     * 工商营业执照
     * @return business_license 工商营业执照
     */
    public String getBusinessLicense() {
        return businessLicense;
    }

    /**
     * 工商营业执照
     * @param businessLicense 工商营业执照
     */
    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense == null ? null : businessLicense.trim();
    }

    /**
     * 食品许可证
     * @return food_license 食品许可证
     */
    public String getFoodLicense() {
        return foodLicense;
    }

    /**
     * 食品许可证
     * @param foodLicense 食品许可证
     */
    public void setFoodLicense(String foodLicense) {
        this.foodLicense = foodLicense == null ? null : foodLicense.trim();
    }

    /**
     * 身份证图片
     * @return card_pic 身份证图片
     */
    public String getCardPic() {
        return cardPic;
    }

    /**
     * 身份证图片
     * @param cardPic 身份证图片
     */
    public void setCardPic(String cardPic) {
        this.cardPic = cardPic == null ? null : cardPic.trim();
    }

    /**
     * 商户营业额
     * @return money 商户营业额
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 商户营业额
     * @param money 商户营业额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 余额
     * @return balance 余额
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 余额
     * @param balance 余额
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * 所在菜场id
     * @return marketid 所在菜场id
     */
    public Integer getMarketid() {
        return marketid;
    }

    /**
     * 所在菜场id
     * @param marketid 所在菜场id
     */
    public void setMarketid(Integer marketid) {
        this.marketid = marketid;
    }

    /**
     * 是否推荐1未推荐
     * @return is_hot 是否推荐1未推荐
     */
    public Byte getIsHot() {
        return isHot;
    }

    /**
     * 是否推荐1未推荐
     * @param isHot 是否推荐1未推荐
     */
    public void setIsHot(Byte isHot) {
        this.isHot = isHot;
    }

    /**
     * 是否推荐1推荐
     * @return is_recommend 是否推荐1推荐
     */
    public Byte getIsRecommend() {
        return isRecommend;
    }

    /**
     * 是否推荐1推荐
     * @param isRecommend 是否推荐1推荐
     */
    public void setIsRecommend(Byte isRecommend) {
        this.isRecommend = isRecommend;
    }

    /**
     * 等级
     * @return grade 等级
     */
    public String getGrade() {
        return grade;
    }

    /**
     * 等级
     * @param grade 等级
     */
    public void setGrade(String grade) {
        this.grade = grade == null ? null : grade.trim();
    }

    /**
     * 摊位门牌号
     * @return address 摊位门牌号
     */
    public String getAddress() {
        return address;
    }

    /**
     * 摊位门牌号
     * @param address 摊位门牌号
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 网易账号
     * @return accid 网易账号
     */
    public String getAccid() {
        return accid;
    }

    /**
     * 网易账号
     * @param accid 网易账号
     */
    public void setAccid(String accid) {
        this.accid = accid == null ? null : accid.trim();
    }

    /**
     * 网易token
     * @return token 网易token
     */
    public String getToken() {
        return token;
    }

    /**
     * 网易token
     * @param token 网易token
     */
    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    /**
     * 手机设备号
     * @return device_number 手机设备号
     */
    public String getDeviceNumber() {
        return deviceNumber;
    }

    /**
     * 手机设备号
     * @param deviceNumber 手机设备号
     */
    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber == null ? null : deviceNumber.trim();
    }

    /**
     * 摄像头id
     * @return cameraid 摄像头id
     */
    public String getCameraid() {
        return cameraid;
    }

    /**
     * 摄像头id
     * @param cameraid 摄像头id
     */
    public void setCameraid(String cameraid) {
        this.cameraid = cameraid == null ? null : cameraid.trim();
    }

    /**
     * 房间号
     * @return roomid 房间号
     */
    public String getRoomid() {
        return roomid;
    }

    /**
     * 房间号
     * @param roomid 房间号
     */
    public void setRoomid(String roomid) {
        this.roomid = roomid == null ? null : roomid.trim();
    }

    /**
     * 2预约中1打样0开张
     * @return status 2预约中1打样0开张
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 2预约中1打样0开张
     * @param status 2预约中1打样0开张
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * 1为菜场自提点
     * @return is_since 1为菜场自提点
     */
    public Boolean getIsSince() {
        return isSince;
    }

    /**
     * 1为菜场自提点
     * @param isSince 1为菜场自提点
     */
    public void setIsSince(Boolean isSince) {
        this.isSince = isSince;
    }

    /**
     * 打印机编号
     * @return printer_num 打印机编号
     */
    public String getPrinterNum() {
        return printerNum;
    }

    /**
     * 打印机编号
     * @param printerNum 打印机编号
     */
    public void setPrinterNum(String printerNum) {
        this.printerNum = printerNum == null ? null : printerNum.trim();
    }

    /**
     * 打印机key
     * @return printer_key 打印机key
     */
    public String getPrinterKey() {
        return printerKey;
    }

    /**
     * 打印机key
     * @param printerKey 打印机key
     */
    public void setPrinterKey(String printerKey) {
        this.printerKey = printerKey == null ? null : printerKey.trim();
    }

    /**
     * 0审核中1审核通过2审核失败
     * @return examine 0审核中1审核通过2审核失败
     */
    public Integer getExamine() {
        return examine;
    }

    /**
     * 0审核中1审核通过2审核失败
     * @param examine 0审核中1审核通过2审核失败
     */
    public void setExamine(Integer examine) {
        this.examine = examine;
    }

    /**
     * 0不显示1显示
     * @return is_show 0不显示1显示
     */
    public Integer getIsShow() {
        return isShow;
    }

    /**
     * 0不显示1显示
     * @param isShow 0不显示1显示
     */
    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    /**
     * 1正式商户0测试商户
     * @return type 1正式商户0测试商户
     */
    public Byte getType() {
        return type;
    }

    /**
     * 1正式商户0测试商户
     * @param type 1正式商户0测试商户
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 0关闭预约1开启预约
     * @return is_appointment 0关闭预约1开启预约
     */
    public Integer getIsAppointment() {
        return isAppointment;
    }

    /**
     * 0关闭预约1开启预约
     * @param isAppointment 0关闭预约1开启预约
     */
    public void setIsAppointment(Integer isAppointment) {
        this.isAppointment = isAppointment;
    }

    /**
     * 推荐值，应急
     * @return recommend 推荐值，应急
     */
    public Integer getRecommend() {
        return recommend;
    }

    /**
     * 推荐值，应急
     * @param recommend 推荐值，应急
     */
    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    /**
     * 审核反馈信息
     * @return examine_info 审核反馈信息
     */
    public String getExamineInfo() {
        return examineInfo;
    }

    /**
     * 审核反馈信息
     * @param examineInfo 审核反馈信息
     */
    public void setExamineInfo(String examineInfo) {
        this.examineInfo = examineInfo == null ? null : examineInfo.trim();
    }

    /**
     * 1小米2华为
     * @return phoneType 1小米2华为
     */
    public String getPhonetype() {
        return phonetype;
    }

    /**
     * 1小米2华为
     * @param phonetype 1小米2华为
     */
    public void setPhonetype(String phonetype) {
        this.phonetype = phonetype == null ? null : phonetype.trim();
    }

    /**
     * 推送token
     * @return pushToken 推送token
     */
    public String getPushtoken() {
        return pushtoken;
    }

    /**
     * 推送token
     * @param pushtoken 推送token
     */
    public void setPushtoken(String pushtoken) {
        this.pushtoken = pushtoken == null ? null : pushtoken.trim();
    }

    /**
     * 0优惠1不优惠
     * @return unFavorable 0优惠1不优惠
     */
    public Integer getUnfavorable() {
        return unfavorable;
    }

    /**
     * 0优惠1不优惠
     * @param unfavorable 0优惠1不优惠
     */
    public void setUnfavorable(Integer unfavorable) {
        this.unfavorable = unfavorable;
    }

    /**
     * 网易云信Token
     * @return login_token 网易云信Token
     */
    public String getLoginToken() {
        return loginToken;
    }

    /**
     * 网易云信Token
     * @param loginToken 网易云信Token
     */
    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken == null ? null : loginToken.trim();
    }

    /**
     * 登录凭证
     * @return yx_token 登录凭证
     */
    public String getYxToken() {
        return yxToken;
    }

    /**
     * 登录凭证
     * @param yxToken 登录凭证
     */
    public void setYxToken(String yxToken) {
        this.yxToken = yxToken == null ? null : yxToken.trim();
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

    /**
     * 商铺模式：0菜场商户1便利店商户
     * @return mode 商铺模式：0菜场商户1便利店商户
     */
    public Byte getMode() {
        return mode;
    }

    /**
     * 商铺模式：0菜场商户1便利店商户
     * @param mode 商铺模式：0菜场商户1便利店商户
     */
    public void setMode(Byte mode) {
        this.mode = mode;
    }
}