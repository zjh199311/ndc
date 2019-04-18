package com.zhongjian.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhongjian.dto.cart.basket.query.HmBasketEditQueryDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultUtil;
import org.apache.log4j.Logger;

import com.zhongjian.common.GsonUtil;
import com.zhongjian.common.ResponseHandle;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.executor.ThreadPoolExecutorSingle;
import com.zhongjian.service.cart.basket.CartBasketService;
import com.zhongjian.service.order.OrderService;
import com.zhongjian.service.pay.GenerateSignatureService;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet(value = "/v1/pay/createsign", asyncSupported = true)
public class CreateSignatureServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(CreateSignatureServlet.class);
	
	private OrderService orderService = (OrderService) SpringContextHolder.getBean(OrderService.class);

	private GenerateSignatureService generateSignatureService = (GenerateSignatureService) SpringContextHolder
			.getBean(GenerateSignatureService.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		AsyncContext asyncContext = request.startAsync();
		ServletInputStream inputStream = request.getInputStream();
		inputStream.setReadListener(new ReadListener() {
			@Override
			public void onDataAvailable() throws IOException {
			}

			@Override
			public void onAllDataRead() {
				ThreadPoolExecutorSingle.executor.execute(() -> {
					String result = null;
					Integer uid = (Integer) request.getAttribute("uid");
					ServletRequest request2 = asyncContext.getRequest();
					String busniess = request2.getParameter("busniess");
					Integer orderid = Integer.valueOf(request2.getParameter("orderid"));
					//0 支付宝 1微信 2微信小程序
					Integer payType = Integer.valueOf(request2.getParameter("paytype"));
					result = CreateSignatureServlet.this.handle(uid, busniess, orderid,payType);
					// 返回数据
					try {
						ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
					} catch (IOException e) {
						log.error("fail cart/edut : " + e.getMessage());
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

	private String handle(Integer uid, String busniess, Integer orderId,Integer payType) {
		if (uid == 0) {
			return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.USER_IS_NULL));
		}
		Map<String, Object>  orderDetail = orderService.getOutTradeNoAndAmount(uid,orderId, busniess);
		if (orderDetail == null)  {
			return GsonUtil.GsonString(ResultUtil.getFail(null));
		}else {
			String out_trade_no = (String) orderDetail.get("out_trade_no");
			String totalPrice =  orderDetail.get("totalPrice").toString();
		if (payType == 0) {
		
			return generateSignatureService.getAliSignature(out_trade_no, totalPrice);
		}else if (payType == 1) {
			return GsonUtil.GsonString(generateSignatureService.getWxAppSignature(out_trade_no, totalPrice, "", "192.168.0.122", 0));
		}else {
			return GsonUtil.GsonString(ResultUtil.getFail(null));
		}
		}
	}

}