package com.zhongjian.dao.dto.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: yd
 */
@Data
public class MessageResDTO implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;
    /**
     * 客户端消息id，使用uuid等随机串，msgId相同的消息会被客户端去重
     */
    private String msgid;
    /**
     * 本消息是否需要过自定义反垃圾系统。true或false, 默认false
     */
    private boolean antispam;
    /**
     * 查询的时间戳锚点，13位。reverse=1时timetag为起始时间戳，reverse=2时timetag为终止时间戳
     */
    private String timetag;


}
