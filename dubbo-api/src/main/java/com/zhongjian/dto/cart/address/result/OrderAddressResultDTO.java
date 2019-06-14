package com.zhongjian.dto.cart.address.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class OrderAddressResultDTO implements Serializable {

    private static final long serialVersionUID = -5948092855255306532L;

    /**
     * 地址id
     */
    private Integer id;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 性别
     */
    private String gender;

    /**
     * 收货电话
     */
    private String phone;

    /**
     * 收获地址
     */
    private String address;

    /**
     * 门牌号
     */
    private String houseNumber;

}
