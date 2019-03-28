package com.zhongjian.service.message.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhongjian.common.config.properties.MessagePushProperties;
import com.zhongjian.common.util.CheckSumBuilderUtil;
import com.zhongjian.common.util.HttpClientUtil;
import com.zhongjian.common.util.MapUtil;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.message.MessageBodyDTO;
import com.zhongjian.dto.message.MessageReqDTO;
import com.zhongjian.dto.message.MessageResDTO;
import com.zhongjian.service.HmBaseTest;
import com.zhongjian.service.message.MessagePushService;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: yd
 */
public class MessagePushServiceImplTest extends HmBaseTest {

    @Resource
    private MessagePushService messagePushService;

    @Resource
    MessagePushProperties messagePushProperties;


    @Test
    public void test1() {
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        String url = "https://api.netease.im/nimserver/msg/sendMsg.action";
        HttpPost httpPost = new HttpPost(url);

        String appKey = "2c9ec0b26e58e5d4c2ab5023c43f3753";
        String appSecret = "ea128df29205";
        String nonce = "12345";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilderUtil.getCheckSum(appSecret, nonce, curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        try {
            // 设置请求的参数
            MessageReqDTO messageReqDTO = new MessageReqDTO();
            MessageBodyDTO messageBodyDTO = new MessageBodyDTO();
            messageBodyDTO.setMsg("nihao");
            messageReqDTO.setFrom(messagePushProperties.getAccid());
            messageReqDTO.setOpe(0);
            messageReqDTO.setTo("j967jc0j9j858725");
            messageReqDTO.setType(100);
            messageReqDTO.setBody(JSONObject.toJSONString(messageBodyDTO));
            HashMap<String, String> map = MapUtil.parseObjectToHashMap(messageReqDTO);
            List<NameValuePair> nameValuePairs = HttpClientUtil.paramsConverter(map);

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);

            // 打印执行结果
            System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test2(){
        MessageReqDTO messageReqDTO = new MessageReqDTO();
        messageReqDTO.setFrom(messagePushProperties.getAccid());
        messageReqDTO.setMsg("你好");
        messageReqDTO.setOpe(0);
        messageReqDTO.setTo("j967jc0j9j858725");
        messageReqDTO.setType(100);
        ResultDTO<MessageResDTO> messageResDTOResultDTO = messagePushService.messagePush(messageReqDTO);
        System.out.println(messageResDTOResultDTO.getData());
    }
}