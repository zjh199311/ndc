package com.zhongjian.service.pay;

import com.zhongjian.dto.common.ResultDTO;

public interface GenerateSignatureService {

    String getAliSignature(String out_trade_no, String totalAmount);

    ResultDTO<Object> getWxAppSignature(String out_trade_no, String totalAmount, String spbillCreateIp);
}
