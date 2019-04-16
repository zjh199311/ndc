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

import com.zhongjian.dto.cart.basket.query.HmBasketDelQueryDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultUtil;
import org.apache.log4j.Logger;

import com.zhongjian.common.GsonUtil;
import com.zhongjian.common.ResponseHandle;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.executor.ThreadPoolExecutorSingle;
import com.zhongjian.service.cart.basket.CartBasketService;

import java.io.IOException;

@WebServlet(value = "/v1/cart/delete", asyncSupported = true)
public class DeleteCartOne extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(DeleteCartOne.class);

	private CartBasketService cartBasketService = (CartBasketService) SpringContextHolder.getBean(CartBasketService.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
					Integer basketId = Integer.valueOf(request2.getParameter("id"));
					result = DeleteCartOne.this.handle(uid, basketId);
					// 返回数据
					try {
						ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
					} catch (IOException e) {
						log.error("fail cart/delete : " + e.getMessage());
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

	private String handle(Integer uid, Integer id) {
		if (uid == 0) {
			return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.USER_IS_NULL));
		}
		HmBasketDelQueryDTO hmBasketDelQueryDTO = new HmBasketDelQueryDTO();
		hmBasketDelQueryDTO.setUid(uid);
		hmBasketDelQueryDTO.setId(id);
		return GsonUtil.GsonString(cartBasketService.deleteInfoById(hmBasketDelQueryDTO));
	}
}