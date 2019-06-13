package com.zhongjian.localservice;

public interface MessageService {

	//起手,用户,商户id body msg
    void messagePush( Integer orderId);
    
    //便利店商户推送单子
    void messagePushCVShop(Integer orderId);
    
  //便利店骑手推送单子
    void messagePushCVRider(Integer rid);
}
