package com.zhongjian.dto.cart.address.result;

import lombok.Data;

import java.io.Serializable;

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
     * 收货人地址
     */
    private String address;

    /**
     * 收货人门牌号
     */
    private String houseNumber;

    /**
     * 默认地址
     */
    private Integer status;

}
