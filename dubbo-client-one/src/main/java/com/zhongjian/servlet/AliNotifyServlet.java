package com.zhongjian.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alipay.api.internal.util.AlipaySignature;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.component.PropUtil;
import com.zhongjian.service.order.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@WebServlet(value = "/v1/notify/ali", asyncSupported = true)
public class AliNotifyServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(AliNotifyServlet.class);
    
	private PropUtil propUtil = (PropUtil) SpringContextHolder.getBean(PropUtil.class);
	
	private OrderService orderService = (OrderService) SpringContextHolder.getBean(OrderService.class);
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// 1.从支付宝回调的request域中取值
			Map<String, String[]> requestParams = request.getParameterMap();
			HashMap<String, String> paramsMap = new HashMap<>();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = iter.next();
				String[] values = requestParams.get(name);
				paramsMap.put(name, values[0]);
			}
			boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, propUtil.getAliPublicKey(),
					propUtil.getAliCharset(), propUtil.getAliSigntype());
			PrintWriter printWriter = response.getWriter();
			if (signVerified) {
				if ("TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
					String out_trade_no = request.getParameter("out_trade_no"); // 商户订单号
					String total_amount = request.getParameter("total_amount"); // 订单金额
					String app_id = request.getParameter("app_id"); // 商家seller_id
					log.info("订单号为 " + out_trade_no + " 支付金额为 " + total_amount + " 开始回调！");
					// 校验四项
					if ( !app_id.equals(propUtil.getAliAppid())) {
						printWriter.print("failure");
					}
					if (orderService.handleROrder(out_trade_no, total_amount)) {
						printWriter.print("success");
					} else {
						printWriter.print("failure");
					}
				} else {
					printWriter.print("failure");
				}
			} else {
				printWriter.print("failure");
			}
		} catch (Exception e) {
			log.error("支付宝异步通知发生异常，请注意处理 " + e );
			response.getWriter().print("failure");
		}

	}

}