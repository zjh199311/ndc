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
import com.zhongjian.service.cart.basket.CartBasketService;
import org.apache.log4j.Logger;

import com.zhongjian.common.GsonUtil;
import com.zhongjian.common.ResponseHandle;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.executor.ThreadPoolExecutorSingle;

import java.io.IOException;

@WebServlet(value = "/cart/add", asyncSupported = true)
public class CartAddServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(CartAddServlet.class);

	private CartBasketService hmBasketService = (CartBasketService) SpringContextHolder.getBean(CartBasketService.class);
	
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
					Integer gid = Integer.valueOf(request2.getParameter("gid"));
					String amount = request2.getParameter("amount");
					String remark = request2.getParameter("remark");
					result = CartAddServlet.this.handle(uid, gid, amount, remark);
					// 返回数据
					try {
						ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
					} catch (IOException e) {
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

	private String handle(Integer uid, Integer gid, String amount, String remark) {
		if (uid == 0) {
			return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.USER_IS_NULL));
		}
		HmBasketEditQueryDTO hmBasketEditQueryDTO = new HmBasketEditQueryDTO();
		hmBasketEditQueryDTO.setUid(uid);
		hmBasketEditQueryDTO.setGid(gid);
		hmBasketEditQueryDTO.setAmount(amount);
		hmBasketEditQueryDTO.setRemark(remark);
		return GsonUtil.GsonString(hmBasketService.addOrUpdateInfo(hmBasketEditQueryDTO));
	}
}