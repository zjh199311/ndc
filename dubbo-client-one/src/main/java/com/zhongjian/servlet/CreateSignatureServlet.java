package com.zhongjian.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultUtil;
import org.apache.log4j.Logger;

import com.zhongjian.common.FormDataUtil;
import com.zhongjian.common.GsonUtil;
import com.zhongjian.common.ResponseHandle;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.executor.ThreadPoolExecutorSingle;
import com.zhongjian.service.order.OrderService;
import com.zhongjian.service.pay.GenerateSignatureService;
import com.zhongjian.service.user.UserService;

import java.io.IOException;
import java.util.Map;

@WebServlet(value = "/v1/pay/createsign", asyncSupported = true)
public class CreateSignatureServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(CreateSignatureServlet.class);

	private OrderService orderService = (OrderService) SpringContextHolder.getBean(OrderService.class);

	private UserService userService = (UserService) SpringContextHolder.getBean(UserService.class);
	
	private GenerateSignatureService generateSignatureService = (GenerateSignatureService) SpringContextHolder
			.getBean(GenerateSignatureService.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String> formData = FormDataUtil.getFormData(request);
		AsyncContext asyncContext = request.startAsync();
		ServletInputStream inputStream = request.getInputStream();
		
		inputStream.setReadListener(new ReadListener() {
			@Override
			public void onDataAvailable() throws IOException {
			}
			
			private String getRealIp(HttpServletRequest request) {
				String ip = request.getHeader("x-forwarded-for");
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("Proxy-Client-IP");
				}
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("WL-Proxy-Client-IP");
				}
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getRemoteAddr();
				}
				return ip;
			}
			
			@Override
			public void onAllDataRead() {
				ThreadPoolExecutorSingle.executor.execute(() -> {
					String result =  GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.SERVERERR));
					try {
					Integer uid = (Integer) request.getAttribute("uid");
					String busniess = formData.get("busniess");// 1.RIO（订单）
					Integer orderid = Integer.valueOf(formData.get("orderid"));
					// 0 支付宝 1微信 2微信小程序
					Integer payType = Integer.valueOf(formData.get("paytype"));
					String openId = formData.get("openid");
					String realIp = this.getRealIp(request);
					result = CreateSignatureServlet.this.handle(uid, busniess, orderid, payType, openId, realIp);
					// 返回数据
					
						ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
					} catch (IOException e) {
						try {
							ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						log.error("fail createsign: " + e.getMessage());
					}
					asyncContext.complete();
				});
			}

			@Override
			public void onError(Throwable t) {
				asyncContext.complete();
			}
		});

	}

	private String handle(Integer uid, String busniess, Integer orderId, Integer payType, String openId,
			String realIp) {
		if (uid == 0) {
			return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.USER_IS_NULL));
		}
		Map<String, Object> orderDetail = orderService.getOutTradeNoAndAmount(uid, orderId, busniess);
		if (orderDetail == null) {
			return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.ORDER_CHANGE));
		} else {
			String out_trade_no = (String) orderDetail.get("out_trade_no");
			String totalPrice = String.valueOf(orderDetail.get("totalPrice"));
			String body = (String) orderDetail.get("body");
			String subject = (String) orderDetail.get("subject");
			if (payType == 0) {
				//支付宝
				return  GsonUtil.GsonString(ResultUtil.getSuccess(generateSignatureService.getAliSignature(out_trade_no, totalPrice,subject)));
			} else if (payType == 1) {
				//微信app支付
				return GsonUtil.GsonString(
						ResultUtil.getSuccess(generateSignatureService.getWxAppSignature(out_trade_no, totalPrice, "", realIp, 0,body)));
			} else if (payType == 2) {
				//小程序支付
				String appletsOpenid = userService.getUserBeanById(uid).getAppletsOpenid();
				if (appletsOpenid == null) {
					return GsonUtil.GsonString(ResultUtil.getFail(null));
				}
				return GsonUtil.GsonString(
						ResultUtil.getSuccess(generateSignatureService.getWxAppSignature(out_trade_no, totalPrice, appletsOpenid, realIp, 1,body)));
			} else {
				//微信银行支付
				return generateSignatureService.getYinHangWxApp(out_trade_no, totalPrice, realIp,body);
			}
		}
	}



}