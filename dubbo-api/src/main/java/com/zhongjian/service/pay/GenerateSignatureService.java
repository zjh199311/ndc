package com.zhongjian.service.pay;

public interface GenerateSignatureService {

	String getAliSignature(String business,String orderId);
}
