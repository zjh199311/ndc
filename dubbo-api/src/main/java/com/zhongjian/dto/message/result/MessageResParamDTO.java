package com.zhongjian.dto.message.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class MessageResParamDTO implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;
    /**
     * 返回组装的data
     */
    private String data;

    /**
     * 描述
     */
    private String desc;

    /**
     * 编码
     */
    private String code;
}
