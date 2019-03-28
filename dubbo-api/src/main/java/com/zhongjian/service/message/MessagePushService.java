package com.zhongjian.service.message;

import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.message.MessageReqDTO;
import com.zhongjian.dto.message.MessageResDTO;

/**
 * @Author: ldd
 */
public interface MessagePushService {

    ResultDTO<MessageResDTO> messagePush(MessageReqDTO messageReqDTO);
}
