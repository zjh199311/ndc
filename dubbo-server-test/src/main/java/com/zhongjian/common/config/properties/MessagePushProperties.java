package com.zhongjian.common.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class MessagePushProperties implements Serializable {

    private static final long serialVersionUID = -8179439680141846273L;

    @Value("${Appkey}")
    private String AppKey;

    @Value("${AppSecret}")
    private String AppSecret;

    @Value("${accid}")
    private String accid;

    @Value("${MsgUrl}")
    private String url;


}
