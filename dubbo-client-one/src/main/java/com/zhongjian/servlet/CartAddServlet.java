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
import com.zhongjian.service.cart.basket.CartBasketService;
import org.apache.log4j.Logger;

import com.zhongjian.common.FormDataUtil;
import com.zhongjian.common.GsonUtil;
import com.zhongjian.common.ResponseHandle;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.executor.ThreadPoolExecutorSingle;

import java.io.IOException;
import java.util.Map;

@WebServlet(value = "/v1/cart/add", asyncSupported = true)
public class CartAddServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(CartAddServlet.class);

	private CartBasketService hmBasketService = (CartBasketService) SpringContextHolder.getBean(CartBasketService.class);
	
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
					Integer gid = Integer.valueOf(formData.get("gid"));
					String amount = formData.get("amount");
					String remark = formData.get("remark");
					String price = formData.get("price");
					Integer sid = formData.get("sid") == null?null:Integer.valueOf(formData.get("sid"));
					if (gid == 0 && sid == null) {
						result = GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.PARAM_LOST));
						ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
						return;
					}
					if (gid != 0) {
						sid = null;
					}
					result = CartAddServlet.this.handle(uid, gid, amount, remark,price,sid);
					// 返回数据
				
						ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
					} catch (Exception e) {
						e.printStackTrace();
						try {
							ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						log.error("fail cart/add : " + e.getMessage());
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

	private String handle(Integer uid, Integer gid, String amount, String remark,String price,Integer sid) {
		if (uid == 0) {
			return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.USER_IS_NULL));
		}
		CartBasketEditQueryDTO cartBasketEditQueryDTO = new CartBasketEditQueryDTO();
		cartBasketEditQueryDTO.setUid(uid);
		cartBasketEditQueryDTO.setGid(gid);
		cartBasketEditQueryDTO.setSid(sid);
		cartBasketEditQueryDTO.setPrice(price);
		cartBasketEditQueryDTO.setAmount(amount);
		cartBasketEditQueryDTO.setRemark(remark);
		return GsonUtil.GsonString(hmBasketService.addOrUpdateInfo(cartBasketEditQueryDTO));
	}
}