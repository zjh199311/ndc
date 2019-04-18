package com.zhongjian.dao.entity.cart.address;

public class CartAddressBean {
    /**
     * 
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 收件人姓名
     */
    private String contacts;

    /**
     * 
     */
    private String gender;

    /**
     * 
     */
    private String phone;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 门牌号
     */
    private String houseNumber;

    /**
     * 1为默认地址
     */
    private Integer status;

    /**
     * 1删除
     */
    private Integer isDelete;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

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
     * 收件人姓名
     * @return contacts 收件人姓名
     */
    public String getContacts() {
        return contacts;
    }

    /**
     * 收件人姓名
     * @param contacts 收件人姓名
     */
    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
    }

    /**
     * 
     * @return gender 
     */
    public String getGender() {
        return gender;
    }

    /**
     * 
     * @param gender 
     */
    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    /**
     * 
     * @return phone 
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 
     * @param phone 
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 收货地址
     * @return address 收货地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 收货地址
     * @param address 收货地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 门牌号
     * @return house_number 门牌号
     */
    public String getHouseNumber() {
        return houseNumber;
    }

    /**
     * 门牌号
     * @param houseNumber 门牌号
     */
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber == null ? null : houseNumber.trim();
    }

    /**
     * 1为默认地址
     * @return status 1为默认地址
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1为默认地址
     * @param status 1为默认地址
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 1删除
     * @return is_delete 1删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 1删除
     * @param isDelete 1删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
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