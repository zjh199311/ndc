package com.zhongjian.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Project Name: kc-risk
 * Package Name: cn.kingcar.risk.biz.common.constant.enums.response
 * Function: 公共响应消息枚举
 * user: San
 * Date:2018/2/5
 */
@AllArgsConstructor
public enum CommonMessageEnum {

    /**
     * 响应消息枚举列表
     */
    SUCCESS("通用成功", 10000),
    FAIL("通用失败", 10001),
    USER_IS_NULL("用户不存在", 11007),
    PARAM_LOST("参数缺失", 10008),
    SHOP_CHANGE("商户状态变化", 12001),
    SERVERERR("服务异常", 20000),
    NO_PERMISION("您没有权限执行此操作", 50000),
    ;

    private @Getter
    String msg;
    private @Getter
    int code;

}
