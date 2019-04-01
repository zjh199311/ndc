package com.zhongjian.service.message;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.message.query.MessageReqDTO;
import com.zhongjian.dto.message.result.MessageResDTO;

/**
 * @Author: ldd
 */
public interface MessagePushService {

    ResultDTO<MessageResDTO> messagePush(MessageReqDTO messageReqDTO);
}
