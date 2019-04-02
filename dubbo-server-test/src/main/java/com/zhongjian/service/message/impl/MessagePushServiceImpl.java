package com.zhongjian.service.message.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhongjian.common.config.properties.MessagePushProperties;
import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.common.constant.enums.response.CommonMessageEnum;
import com.zhongjian.common.util.CheckSumBuilderUtil;
import com.zhongjian.common.util.HttpClientUtil;
import com.zhongjian.common.util.LogUtil;
import com.zhongjian.common.util.MapUtil;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.message.query.MessageBodyDTO;
import com.zhongjian.dto.message.query.MessageReqDTO;
import com.zhongjian.dto.message.result.MessageResDTO;
import com.zhongjian.dto.message.result.MessageResParamDTO;
import com.zhongjian.service.message.MessagePushService;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: ldd
 */
@Service
@com.alibaba.dubbo.config.annotation.Service(interfaceClass =MessagePushService.class,retries = 0)
public class MessagePushServiceImpl implements MessagePushService {

    @Resource
    private MessagePushProperties messagePushProperties;


    @Override
    public ResultDTO<MessageResDTO> messagePush(MessageReqDTO messageReqDTO) {
        ResultDTO<MessageResDTO> resultDTO = new ResultDTO<MessageResDTO>();
        resultDTO.setFlag(false);

        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        String url = messagePushProperties.getUrl();
        HttpPost httpPost = new HttpPost(url);

        String appKey = messagePushProperties.getAppKey();
        String appSecret = messagePushProperties.getAppSecret();
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
            MessageBodyDTO messageBodyDTO = new MessageBodyDTO();
            messageBodyDTO.setMsg(messageReqDTO.getMsg());
            messageReqDTO.setBody(JSONObject.toJSONString(messageBodyDTO));
            HashMap<String, String> map = MapUtil.parseObjectToHashMap(messageReqDTO);
            List<NameValuePair> nameValuePairs = HttpClientUtil.paramsConverter(map);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String message = EntityUtils.toString(response.getEntity(), "utf-8");
            MessageResParamDTO messageResParamDTO = JSONObject.parseObject(message, MessageResParamDTO.class);
            if(FinalDatas.NUMBER.equals(messageResParamDTO.getCode())){
                LogUtil.info("发送成功","状态码:"+messageResParamDTO.getCode());
                MessageResDTO messageResDTO = JSONObject.parseObject(messageResParamDTO.getData(),MessageResDTO.class);
                resultDTO.setStatusCode(messageResParamDTO.getCode());
                if(null!=messageResDTO){
                    resultDTO.setData(messageResDTO);
                    resultDTO.setFlag(true);
                    resultDTO.setErrorMessage(CommonMessageEnum.SUCCESS.getMsg());
                }
            }else{
                LogUtil.info("发送失败","");
                resultDTO.setErrorMessage(CommonMessageEnum.FAIL.getMsg());
                resultDTO.setErrorMessage(CommonMessageEnum.FAIL.getCode());
            }
        } catch (Exception e) {
            LogUtil.info(e,"出现异常");
            resultDTO.setErrorMessage(CommonMessageEnum.FAIL.getMsg());
            resultDTO.setErrorMessage(CommonMessageEnum.FAIL.getCode());
            return resultDTO;
        }finally{
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultDTO;
    }
}
