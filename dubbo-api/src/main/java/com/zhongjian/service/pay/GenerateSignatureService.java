package com.zhongjian.service.pay;

import com.zhongjian.dto.common.ResultDTO;

public interface GenerateSignatureService {

    String getAliSignature(String business, String orderId);

    ResultDTO<Object> getWxAppSignature(String business, String orderId, String spbillCreateIp);
}
