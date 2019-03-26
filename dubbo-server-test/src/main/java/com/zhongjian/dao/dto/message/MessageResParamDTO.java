package com.zhongjian.dao.dto.message;

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
     * 编码
     */
    private String code;
}
