package com.zhongjian.common.constant.enums.hm.basket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: ldd
 */
@AllArgsConstructor
public enum HmBasketEnum {

    AMOUNT_IS_NULL("数量为空或者数量为0","AMOUNT_IS_NULL"),

    GID_IS_NULL("商品id为空","GID_IS_NULL"),

    SID_IS_NULL("商户id为空","SID_IS_NULL"),

    LOGINTOKEN_IS_NULL("登入token为空","LOGINTOKEN_IS_NULL"),


    ;

    private @Setter @Getter String msg;

    private @Setter @Getter String code;


}
