package com.zhongjian.service.pay;

import java.util.SortedMap;

public interface GenerateSignatureService {

    String getAliSignature(String out_trade_no, String totalAmount);

    SortedMap<String, String> getWxAppSignature(String out_trade_no, String totalAmount, String openId, String spbillCreateIp, Integer type);


}
