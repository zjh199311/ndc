package com.zhongjian.service.message;

import com.zhongjian.dto.message.result.MessageResParamDTO;

/**
 * @Author: ldd
 */
public interface MessagePushService {


    //起手,用户,商户id body msg
    void messagePush(String rid, String uid, String[] pid);

}
