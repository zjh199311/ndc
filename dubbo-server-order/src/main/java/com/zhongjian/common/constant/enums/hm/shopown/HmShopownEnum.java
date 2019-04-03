package com.zhongjian.common.constant.enums.hm.shopown;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: ldd
 */
@AllArgsConstructor
public enum HmShopownEnum {

    PID_IS_NULL("商户id为空","PID_IS_NULL"),

    STATUS_IS_NULL("效验状态为空","STATUS_IS_NULL"),

    ;

    private @Setter @Getter String msg;

    private @Setter @Getter String code;


}
