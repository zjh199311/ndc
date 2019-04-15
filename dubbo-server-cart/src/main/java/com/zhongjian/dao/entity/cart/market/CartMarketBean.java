package com.zhongjian.dao.entity.cart.market;

public class CartMarketBean {
    /**
     * 
     */
    private Integer id;

    /**
     * 菜场名称
     */
    private String ename;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区域
     */
    private String area;

    /**
     * 摊位地址
     */
    private String address;

    /**
     * 添加时间
     */
    private Integer ctime;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 开始时间
     */
    private String starttime;

    /**
     * 菜场关门时间
     */
    private String endtime;

    /**
     * 0未开通1已开通
     */
    private Byte state;

    /**
     * 面积
     */
    private String size;

    /**
     * 菜场图片
     */
    private String marketPic;

    /**
     * 1存在0删除
     */
    private Byte isDelete;

    /**
     * 活动0已关闭1已开启
     */
    private Integer status;

    /**
     * 活动内容
     */
    private String activity;

    /**
     * 不配送区域
     */
    private String noPay;

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
     * 菜场名称
     * @return ename 菜场名称
     */
    public String getEname() {
        return ename;
    }

    /**
     * 菜场名称
     * @param ename 菜场名称
     */
    public void setEname(String ename) {
        this.ename = ename == null ? null : ename.trim();
    }

    /**
     * 联系人
     * @return contacts 联系人
     */
    public String getContacts() {
        return contacts;
    }

    /**
     * 联系人
     * @param contacts 联系人
     */
    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
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
     * 省份
     * @return province 省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * 省份
     * @param province 省份
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * 城市
     * @return city 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 城市
     * @param city 城市
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * 区域
     * @return area 区域
     */
    public String getArea() {
        return area;
    }

    /**
     * 区域
     * @param area 区域
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    /**
     * 摊位地址
     * @return address 摊位地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 摊位地址
     * @param address 摊位地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 添加时间
     * @return ctime 添加时间
     */
    public Integer getCtime() {
        return ctime;
    }

    /**
     * 添加时间
     * @param ctime 添加时间
     */
    public void setCtime(Integer ctime) {
        this.ctime = ctime;
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
     * 开始时间
     * @return starttime 开始时间
     */
    public String getStarttime() {
        return starttime;
    }

    /**
     * 开始时间
     * @param starttime 开始时间
     */
    public void setStarttime(String starttime) {
        this.starttime = starttime == null ? null : starttime.trim();
    }

    /**
     * 菜场关门时间
     * @return endtime 菜场关门时间
     */
    public String getEndtime() {
        return endtime;
    }

    /**
     * 菜场关门时间
     * @param endtime 菜场关门时间
     */
    public void setEndtime(String endtime) {
        this.endtime = endtime == null ? null : endtime.trim();
    }

    /**
     * 0未开通1已开通
     * @return state 0未开通1已开通
     */
    public Byte getState() {
        return state;
    }

    /**
     * 0未开通1已开通
     * @param state 0未开通1已开通
     */
    public void setState(Byte state) {
        this.state = state;
    }

    /**
     * 面积
     * @return size 面积
     */
    public String getSize() {
        return size;
    }

    /**
     * 面积
     * @param size 面积
     */
    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    /**
     * 菜场图片
     * @return market_pic 菜场图片
     */
    public String getMarketPic() {
        return marketPic;
    }

    /**
     * 菜场图片
     * @param marketPic 菜场图片
     */
    public void setMarketPic(String marketPic) {
        this.marketPic = marketPic == null ? null : marketPic.trim();
    }

    /**
     * 1存在0删除
     * @return is_delete 1存在0删除
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * 1存在0删除
     * @param isDelete 1存在0删除
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 活动0已关闭1已开启
     * @return status 活动0已关闭1已开启
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 活动0已关闭1已开启
     * @param status 活动0已关闭1已开启
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 活动内容
     * @return activity 活动内容
     */
    public String getActivity() {
        return activity;
    }

    /**
     * 活动内容
     * @param activity 活动内容
     */
    public void setActivity(String activity) {
        this.activity = activity == null ? null : activity.trim();
    }

    /**
     * 不配送区域
     * @return no_pay 不配送区域
     */
    public String getNoPay() {
        return noPay;
    }

    /**
     * 不配送区域
     * @param noPay 不配送区域
     */
    public void setNoPay(String noPay) {
        this.noPay = noPay == null ? null : noPay.trim();
    }
}