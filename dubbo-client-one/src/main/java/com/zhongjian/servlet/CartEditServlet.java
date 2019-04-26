package com.zhongjian.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhongjian.dto.cart.basket.query.CartBasketEditQueryDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultUtil;
import org.apache.log4j.Logger;

import com.zhongjian.common.FormDataUtil;
import com.zhongjian.common.GsonUtil;
import com.zhongjian.common.ResponseHandle;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.executor.ThreadPoolExecutorSingle;
import com.zhongjian.service.cart.basket.CartBasketService;

import java.io.IOException;
import java.util.Map;

@WebServlet(value = "/v1/cart/edit", asyncSupported = true)
public class CartEditServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(CartEditServlet.class);

	private CartBasketService hmBasketService = (CartBasketService) SpringContextHolder
			.getBean(CartBasketService.class);

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
				
					Integer id= Integer.valueOf(formData.get("id"));
					String amount = formData.get("amount");
					String remark = formData.get("remark");
					String price = formData.get("price");					
					result = CartEditServlet.this.handle(id,uid, amount, remark,price);
						// 返回数据
						ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
					} catch (Exception e) {
						try {
							ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						log.error("fail cart/edit : " + e.getMessage());
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

	private String handle(Integer id, Integer uid, String amount, String remark,String price) {
		if (uid == 0) {
			return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.USER_IS_NULL));
		}
		CartBasketEditQueryDTO cartBasketEditQueryDTO = new CartBasketEditQueryDTO();
		cartBasketEditQueryDTO.setUid(uid);
		cartBasketEditQueryDTO.setId(id);
		cartBasketEditQueryDTO.setAmount(amount);
		cartBasketEditQueryDTO.setRemark(remark);
		cartBasketEditQueryDTO.setPrice(price);
		return GsonUtil.GsonString(hmBasketService.editInfo(cartBasketEditQueryDTO));
	}
}