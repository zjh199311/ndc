package com.zhongjian.dao.entity.order.address;

public class OrderAddressOrderBean {

	private String riderSn;
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

	public String getRiderSn() {
		return riderSn;
	}

	public void setRiderSn(String riderSn) {
		this.riderSn = riderSn;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public Integer getCtime() {
		return ctime;
	}

	public void setCtime(Integer ctime) {
		this.ctime = ctime;
	}

    
}
