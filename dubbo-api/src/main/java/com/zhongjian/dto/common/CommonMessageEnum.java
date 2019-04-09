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
    SUCCESS("操作成功", "SUCCESS"),
    FAIL("操作失败", "FAIL"),
    PARAM_LOST("参数缺失", "PARAM_LOST"),
    ILLEGAL_PARAM("非法参数", "ILLEGAL_PARAM"),
    NO_PERMISION("您没有权限执行此操作", "NO_PERMISION"),
    DATA_IS_EMPTY("数据为空", "DATA_IS_EMPTY"),
    PAGE_IS_EMPTY("分页数据为空", "PAGE_IS_EMPTY"),
    PRI_ID_IS_EMPT("主键ID为空", "PAGE_IS_EMPTY"),
    USER_IS_NULL("用户不存在", "USER_IS_NULL"),
    USER_IS_EXIST("用户已存在", "USER_IS_EXIST"),

    //---------------------------------------购物车枚举-----------------------
    AMOUNT_IS_NULL("数量为空或者数量为0", "AMOUNT_IS_NULL"),

    GID_IS_NULL("商品id为空", "GID_IS_NULL"),

    SID_IS_NULL("商户id为空", "SID_IS_NULL"),

    UID_IS_NULL("用户id为空", "UID_IS_NULL"),
    
    //订单
    PID_IS_NULL("商户id为空","PID_IS_NULL"),

    STATUS_IS_NULL("效验状态为空","STATUS_IS_NULL"),
    ;
    private @Getter
    String msg;
    private @Getter
    String code;

}
