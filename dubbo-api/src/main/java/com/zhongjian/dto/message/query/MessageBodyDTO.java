package com.zhongjian.dto.message.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: yd
 */
@Data
public class MessageBodyDTO implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;
    /**
     * 消息封装的字段
     */
    private String msg;

}
