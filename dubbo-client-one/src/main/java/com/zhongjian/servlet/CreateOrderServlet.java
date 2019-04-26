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
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.dto.order.order.query.OrderStatusQueryDTO;

import org.apache.log4j.Logger;

import com.zhongjian.common.FormDataUtil;
import com.zhongjian.common.GsonUtil;
import com.zhongjian.common.ResponseHandle;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.executor.ThreadPoolExecutorSingle;
import com.zhongjian.service.order.OrderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(value = "/v1/order/createorder", asyncSupported = true)
public class CreateOrderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(CreateOrderServlet.class);

	private OrderService orderService = (OrderService) SpringContextHolder.getBean(OrderService.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String> formData = FormDataUtil.getFormData(request);
		AsyncContext asyncContext = request.startAsync();
		ServletInputStream inputStream = request.getInputStream();
		inputStream.setReadListener(new ReadListener() {
			@Override
			public void onDataAvailable() throws IOException {
			}

			@Override
			public void onAllDataRead() {
				ThreadPoolExecutorSingle.executor.execute(() -> {
					String result = GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.SERVERERR));
					try {
						Integer uid = (Integer) request.getAttribute("uid");
						String sid = formData.get("sid");
						String[] sidsString = sid.split(",");
						Integer[] sids = new Integer[sidsString.length];
						for (int i = 0; i < sidsString.length; i++) {
							sids[i] = Integer.valueOf(sidsString[i]);
						}
						String type = formData.get("type");
						Integer extra = Integer.valueOf(formData.get("extra"));
						if ("2".equals(type) && extra == null) {
							result = GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.PARAM_LOST));
							ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
							return;
						}
						String isSelfMention = formData.get("isselfmention");
						Integer addressId = Integer.valueOf(formData.get("adressid"));
						Integer unixTime = Integer.valueOf(formData.get("unixtime"));
						Integer status = Integer.valueOf(formData.get("status"));
						if (status == 1) {
							 result = GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.SHOP_CHANGE));
							 ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
							 return;
						}
						result = CreateOrderServlet.this.handle(uid,sids,type,extra,isSelfMention,addressId,unixTime,status);
						ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
					} catch (Exception e) {
						e.printStackTrace();
						try {
							ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						log.error("fail createorder: " + e.getMessage());
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

	private String handle(Integer uid, Integer[] sids, String type, Integer extra, String isSelfMention,
			Integer addressId,Integer unixTime,Integer status) {
		if (uid == 0) {
			return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.USER_IS_NULL));
		}
		//check shop status
		OrderStatusQueryDTO orderStatusQueryDTO = new OrderStatusQueryDTO();
		List<Integer> sidList = new ArrayList<Integer>();
		for (int i = 0; i < sids.length; i++) {
		 sidList.add(sids[i]);	
		}
		orderStatusQueryDTO.setPids(sidList);
		orderStatusQueryDTO.setStatus(status);
		ResultDTO<String> jungle = orderService.judgeHmShopownStatus(orderStatusQueryDTO);
		if (jungle.getData().equals("1")) {
			return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.SHOP_CHANGE));
		}else if (jungle.getData().equals("2")) {
			return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.SHOP_CHANGE_ADVANCE));
		}else if (jungle.getData().equals("3")){
			return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.SHOP_CHANGE_OPEN));
		}
		Integer isAppointment = 0;
		if (status == 2) {
			isAppointment = 1;
		}
		Map<String, Object> reslutMap = orderService.previewOrCreateOrder(uid, sids, type, extra, isSelfMention, true, addressId, unixTime, isAppointment);
		return GsonUtil.GsonString(ResultUtil.getSuccess(reslutMap));
	}
	
}