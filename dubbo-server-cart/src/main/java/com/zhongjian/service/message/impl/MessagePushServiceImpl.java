package com.zhongjian.service.message.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.commoncomponent.PropUtil;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.message.query.MessageBodyDTO;
import com.zhongjian.dto.message.query.MessageReqDTO;
import com.zhongjian.dto.message.result.MessageResDTO;
import com.zhongjian.dto.message.result.MessageResParamDTO;
import com.zhongjian.service.message.MessagePushService;
import com.zhongjian.util.CheckSumBuilderUtil;
import com.zhongjian.util.HttpConnectionPoolUtil;
import com.zhongjian.util.LogUtil;
import com.zhongjian.util.MapUtil;


import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

/**
 * @Author: ldd
 */
@Service
public class MessagePushServiceImpl implements MessagePushService {

	
   @Autowired
   private PropUtil propUtil ;
    @Override
    public ResultDTO<MessageResDTO> messagePush(MessageReqDTO messageReqDTO) {
        ResultDTO<MessageResDTO> resultDTO = new ResultDTO<MessageResDTO>();
        resultDTO.setFlag(false);
        String appKey = propUtil.getYxAppKey();
        String appSecret = propUtil.getYxAppSecret();
        String nonce = "12345";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilderUtil.getCheckSum(appSecret, nonce, curTime);//参考 计算CheckSum的java代码
        HttpPost httpPost = new HttpPost(propUtil.getYxUrl());
        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        try {
            // 设置请求的参数
            MessageBodyDTO messageBodyDTO = new MessageBodyDTO();
            //设置from
            messageReqDTO.setFrom(propUtil.getYxAccid());
            messageBodyDTO.setMsg(messageReqDTO.getMsg());
            messageReqDTO.setBody(JSONObject.toJSONString(messageBodyDTO));
            HashMap<String, String> map = MapUtil.parseObjectToHashMap(messageReqDTO);
            String message = HttpConnectionPoolUtil.post(propUtil.getYxUrl(), httpPost,map);
            // 执行请求
            MessageResParamDTO messageResParamDTO = JSONObject.parseObject(message, MessageResParamDTO.class);
            if(FinalDatas.NUMBER.equals(messageResParamDTO.getCode())){
                LogUtil.info("发送成功","状态码:"+messageResParamDTO.getCode());
                MessageResDTO messageResDTO = JSONObject.parseObject(messageResParamDTO.getData(),MessageResDTO.class);
                resultDTO.setCode(CommonMessageEnum.SUCCESS.getCode());
                if(null!=messageResDTO){
                    resultDTO.setData(messageResDTO);
                    resultDTO.setFlag(true);
                    resultDTO.setMsg(CommonMessageEnum.SUCCESS.getMsg());
                }
            }else{
                LogUtil.info("发送失败","");
                resultDTO.setMsg(CommonMessageEnum.FAIL.getMsg());
                resultDTO.setCode(CommonMessageEnum.FAIL.getCode());
            }
        } catch (Exception e) {
            LogUtil.info(e,"出现异常");
            resultDTO.setMsg(CommonMessageEnum.FAIL.getMsg());
            resultDTO.setCode(CommonMessageEnum.FAIL.getCode());
            return resultDTO;
        }finally{
        }
        return resultDTO;
    }
}
