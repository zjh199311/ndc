package com.zhongjian.dto.cart.address.result;

import lombok.Data;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @Author: ldd
 */
@Data
public class CartAddressResultDTO implements Serializable{

    private static final long serialVersionUID = -5948092855255306532L;

    /**
     * 地址id
     */
    private Integer id;

    /**
     * 收货人姓名
     */
    private String contacts;

    /**
     * 性别
     */
    private String gender;

    /**
     * 收货人手机号
     */
    private String phone;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 收货人地址
     */
    private String address;

    /**
     * 收货人门牌号
     */
    @SerializedName(value = "house_number")
    private String houseNumber;

    /**
     * 默认地址
     */
    @Expose(serialize =  false)
    private Integer status;

}
