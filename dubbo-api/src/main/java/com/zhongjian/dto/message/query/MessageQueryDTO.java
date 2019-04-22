package com.zhongjian.dto.message.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class MessageQueryDTO implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 消息
     */
    private String imMessage;

    /**
     * 内容
     */
    private String content;
}
