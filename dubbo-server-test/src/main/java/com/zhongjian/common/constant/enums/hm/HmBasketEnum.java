package com.zhongjian.common.constant.enums.hm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: ldd
 */
@AllArgsConstructor
public enum HmBasketEnum {

    AMOUNTISNULL("数量为空","GIDISNULL"),

    GIDISNULL("商品id为空","GIDISNULL"),

    LOGINTOKENISNULL("登入token为空","LOGINTOKENISNULL"),


    ;

    private @Setter @Getter String msg;

    private @Setter @Getter String code;


}
