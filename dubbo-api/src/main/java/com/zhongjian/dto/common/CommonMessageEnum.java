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
    SUCCESS("操作成功","SUCCESS"),
    FAIL("操作失败","FAIL"),
    PARAM_LOST("参数缺失","PARAM_LOST"),
    ILLEGAL_PARAM("非法参数","ILLEGAL_PARAM"),
    NO_PERMISION("您没有权限执行此操作","NO_PERMISION"),
    DATA_IS_EMPTY("数据为空","DATA_IS_EMPTY"),
    PAGE_IS_EMPTY("分页数据为空","PAGE_IS_EMPTY"),
    PRI_ID_IS_EMPT("主键ID为空","PAGE_IS_EMPTY"),
    USER_IS_NULL("用户不存在","USER_IS_NULL"),
    USER_IS_EXIST("用户已存在","USER_IS_EXIST"),;
    private @Getter String msg;
    private @Getter String code;

}
