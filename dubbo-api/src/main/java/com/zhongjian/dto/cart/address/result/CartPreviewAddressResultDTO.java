package com.zhongjian.dto.cart.address.result;

import java.io.Serializable;

import lombok.Data;

@Data
public class CartPreviewAddressResultDTO implements Serializable {
    /**
     * 
     */
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

}
