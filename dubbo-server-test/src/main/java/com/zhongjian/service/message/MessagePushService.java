package com.zhongjian.service.message;

import com.zhongjian.dao.dto.message.MessageReqDTO;
import com.zhongjian.dao.dto.message.MessageResDTO;
import com.zhongjian.dto.common.ResultDTO;

/**
 * @Author: ldd
 */
public interface MessagePushService {

    ResultDTO<MessageResDTO> messagePush(MessageReqDTO messageReqDTO);
}
