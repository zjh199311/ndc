package com.zhongjian.service.message.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.commoncomponent.PropUtil;
import com.zhongjian.dto.message.query.MessageBodyDTO;
import com.zhongjian.dto.message.query.MessageQueryDTO;
import com.zhongjian.dto.message.query.MessageReqDTO;
import com.zhongjian.dto.message.result.MessageResParamDTO;
import com.zhongjian.service.message.MessagePushService;
import com.zhongjian.util.*;
import org.apache.commons.lang3.StringUtils;
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
	private PropUtil propUtil;

	@Override
	public void messagePush(String rid, String uid, String[] pid) {
		MessageReqDTO messageDTO = new MessageReqDTO();
		messageDTO.setOpe(0);
		messageDTO.setType(100);
		messageDTO.setOption("{\"push\":true,\"roam\":true,\"history\":false,\"sendersync\":true, \"route\":false}");
		if (!StringUtils.isBlank(rid)) {
			messageDTO.setTo(String.valueOf(rid));
			messageDTO.setPushcontent("您有新订单了");
			messageDTO.setPayload("{\n" + "          \"key1\": \"value1\",\n" + "          \"apsField\": {\n"
					+ "              \"mutable-content\": 1,\n" + "              \"sound\": \"warningMusic.caf\",\n"
					+ "              \"alert\": {\n" + "                  \"title\": \"订单提醒\",\n"
					+ "                  \"body\": \"您有新的订单了!\"\n" + "              }\n" + "           },\n"
					+ "           \"CustomNews\": {\n" + "              \"type\": 1,\n"
					+ "              \"data\": \"您有新的订单了!\"\n" + "           }\n" + "      }");
			messageDTO.setMsg("你有一份新订单");
			messageDTO.setRoleType(1);
			messagePush(messageDTO);
		}
		if (!StringUtil.isBlank(uid)) {
			messageDTO.setMsg(null);
			messageDTO.setTo(String.valueOf(uid));
			messageDTO.setPushcontent("您的订单已被接单,请耐心等待配送");
			messageDTO.setRoleType(1001);
			messageDTO.setPayload(null);
			messageDTO.setContent("您的订单已被接单,请耐心等待配送");
			messagePush(messageDTO);
		}
		if (null != pid && pid.length != 0) {
			messageDTO.setPushcontent("");
			messageDTO.setTo(String.valueOf(pid));
			messageDTO.setRoleType(1000);
			messagePush(messageDTO);
		}
	}

	public  void messagePush(MessageReqDTO messageReqDTO) {
		String appKey = propUtil.getYxAppKey();
		String appSecret = propUtil.getYxAppSecret();
		String nonce = "12345";
		String curTime = String.valueOf((new Date()).getTime() / 1000L);
		String checkSum = CheckSumBuilderUtil.getCheckSum(appSecret, nonce, curTime);// 参考 计算CheckSum的java代码
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
			MessageQueryDTO messageQueryDTO = new MessageQueryDTO();
			if (!StringUtil.isBlank(messageReqDTO.getMsg())) {
				messageQueryDTO.setImMessage(messageReqDTO.getMsg());
			}
			if (!StringUtil.isBlank(messageReqDTO.getContent())) {
				messageQueryDTO.setContent(messageReqDTO.getContent());
			}
			// 设置from
			messageReqDTO.setFrom(propUtil.getYxAccid());
			messageBodyDTO.setData(JSONObject.toJSONString(messageQueryDTO));
			messageBodyDTO.setType(messageReqDTO.getRoleType());
			messageReqDTO.setBody(JSONObject.toJSONString(messageBodyDTO));
			HashMap<String, String> map = MapUtil.parseObjectToHashMap(messageReqDTO);
			String message = HttpConnectionPoolUtil.post(propUtil.getYxUrl(), httpPost, map);
			// 执行请求
			MessageResParamDTO messageResParamDTO = JSONObject.parseObject(message, MessageResParamDTO.class);
			if (FinalDatas.NUMBER.equals(messageResParamDTO.getCode())) {
				LogUtil.info("发送成功", "状态码:" + messageResParamDTO.getCode());
			} else {
				LogUtil.info("发送失败", "" + messageResParamDTO.getDesc());
			}
		} catch (Exception e) {
			LogUtil.info(e, "出现异常");
		}
	}

}
