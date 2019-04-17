package com.zhongjian.service.pay;

import java.util.Map;

public interface GenerateSignatureService {

    String getAliSignature(String out_trade_no, String totalAmount);

    Map<String, String> getWxAppSignature(String out_trade_no, String totalAmount, String openId, String spbillCreateIp, Integer type);


}
