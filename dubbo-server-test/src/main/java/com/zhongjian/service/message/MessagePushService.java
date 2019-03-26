package com.zhongjian.service.message;

import com.zhongjian.common.dto.ResultDTO;
import com.zhongjian.dao.dto.message.MessageReqDTO;
import com.zhongjian.dao.dto.message.MessageResDTO;

/**
 * @Author: ldd
 */
public interface MessagePushService {

    ResultDTO<MessageResDTO> messagePush(MessageReqDTO messageReqDTO);
}
