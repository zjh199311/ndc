package com.zhongjian.localservice.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.zhongjian.common.constant.FinalDatas;
import com.zhongjian.commoncomponent.PropUtil;
import com.zhongjian.dao.jdbctemplate.OrderDao;
import com.zhongjian.dao.jdbctemplate.UserDao;
import com.zhongjian.dto.message.query.MessageReqDTO;
import com.zhongjian.dto.message.result.MessageResParamDTO;
import com.zhongjian.localservice.MessageService;
import com.zhongjian.util.CheckSumBuilderUtil;
import com.zhongjian.util.HttpConnectionPoolUtil;
import com.zhongjian.util.LogUtil;
import com.zhongjian.util.MapUtil;

public class MessageServiceImpl implements MessageService {

	@Autowired
	private PropUtil propUtil;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private UserDao userDao;

	@Override
	public void messagePush(Integer orderId) {
		
		Map<String, Object> riderOrderDetail = orderDao.getDetailByOrderId(orderId);
		
		String uAccid = userDao.getAccidById((Integer) riderOrderDetail.get("uid"));
		if (!"".equals(uAccid)) {
			pushMessageToUser(uAccid);
		}
		
		Object ridObj = riderOrderDetail.get("rid");
		if (ridObj != null) {
			String rAccid = userDao.getAccidById((Integer) ridObj);
			if (!"".equals(rAccid)) {
				pushMessageToRider(rAccid);
			}
		}

		List<Map<String, Object>> sOrders = orderDao.getOrderDetailByRoid(orderId);
		for (Map<String, Object> sOrder : sOrders) {
			String orderSn = (String) sOrder.get("order_sn");
			Integer isAppointment = (Integer) sOrder.get("is_appointment");
			Integer pid = (Integer) sOrder.get("pid");
			String shopAccid = userDao.getShopAccidById(pid);
		}
	   
		
	}

	// 发消息给用户
	private void pushMessageToUser(String uAccid) {
		MessageReqDTO umessageDTO = new MessageReqDTO();
		umessageDTO.setOpe(0);
		umessageDTO.setType(100);
		umessageDTO.setTo(uAccid);
		umessageDTO.setOption("{\"push\":true,\"roam\":true,\"history\":false,\"sendersync\":true, \"route\":false}");
		umessageDTO.setPushcontent("您的订单已被接单,请耐心等待配送");
		String body = "{\"type\" : 1001, \"data\" : {\"content\": \"您的订单已被接单,请耐心等待配送\"}}";
		umessageDTO.setBody(body);
		messagePush(umessageDTO);
	}

	// 发消息给骑手
	private void pushMessageToRider(String rAccid) {
		MessageReqDTO rmessageDTO = new MessageReqDTO();
		rmessageDTO.setOpe(0);
		rmessageDTO.setType(100);
		rmessageDTO.setTo(rAccid);
		rmessageDTO.setOption("{\"push\":true,\"roam\":true,\"history\":false,\"sendersync\":true, \"route\":false}");
		rmessageDTO.setPushcontent("您有新订单了");
		rmessageDTO.setPayload("{\"key1\": \"value1\",\"apsField\": {"
				+ "\"mutable-content\": 1,\"sound\": \"warningMusic.caf\"," + "\"alert\": { \"title\": \"订单提醒\","
				+ "\"body\": \"您有新的订单了!\"}}," + "\"CustomNews\": {\"type\": 1," + "\"data\": \"您有新的订单了!\"}}");
		String body = "{\"type\" : 1, \"data\" : {\"imMessage\": \"你有一份新订单\"}}";
		rmessageDTO.setBody(body);
		messagePush(rmessageDTO);
	}

	// 发消息给用户
	private void pushMessageToSids(String sAccid) {
		MessageReqDTO smessageDTO = new MessageReqDTO();
		smessageDTO.setOpe(0);
		smessageDTO.setType(100);
		smessageDTO.setTo(sAccid);
		smessageDTO.setOption("{\"push\":false,\"roam\":true,\"history\":false,\"sendersync\":true, \"route\":false}");
	}

	public void messagePush(MessageReqDTO messageReqDTO) {
		String appKey = propUtil.getYxAppKey();
		String appSecret = propUtil.getYxAppSecret();
		String nonce = String.valueOf(new Date().getTime());
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
			// 设置from
			messageReqDTO.setFrom(propUtil.getYxAccid());
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
