package com.zhongjian.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhongjian.dto.cart.address.query.CartAddressQueryDTO;
import com.zhongjian.dto.cart.address.result.CartAddressResultDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.exception.NDCException;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.rpc.RpcContext;
import com.zhongjian.common.FormDataUtil;
import com.zhongjian.common.GsonUtil;
import com.zhongjian.common.ResponseHandle;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.executor.ThreadPoolExecutorSingle;
import com.zhongjian.service.address.AddressService;
import com.zhongjian.service.order.CVOrderService;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@WebServlet(value = "/v1/order/previewcvorder", asyncSupported = true)
public class PreviewCVOrderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(PreviewCVOrderServlet.class);

	private CVOrderService orderService = (CVOrderService) SpringContextHolder.getBean(CVOrderService.class);

	private AddressService addressServie = (AddressService) SpringContextHolder.getBean(AddressService.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
						//sid,login_token 
						Integer uid = (Integer) request.getAttribute("uid");
						Integer sid = Integer.valueOf(formData.get("sid"));
						String type = formData.get("type")==null?"0":formData.get("type");
						String extraString = formData.get("extra");
						if ("2".equals(type) && extraString == null) {
							result = GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.PARAM_LOST));
							ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
							asyncContext.complete();
							return;
						}
						Integer extra = null;
						if ("2".equals(type)) {
							extra = Integer.valueOf(extraString);
						}
						String isSelfMention = formData.get("isselfmention") == null?"0":formData.get("isselfmention");
						result = PreviewCVOrderServlet.this.handle(uid,sid,type,extra,isSelfMention);
						ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
					} catch (Exception e) {
						e.printStackTrace();
						try {
							ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						log.error("fail previeworder: " + e.getMessage());
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

	private String handle(Integer uid, Integer sid, String type, Integer extra,String isselfmention) throws NDCException {
		if (uid == 0) {
			return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.USER_IS_NULL));
		}
		//check shop status
		if (!orderService.judgeHmShopownStatus(sid)) {
			return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.SHOP_CHANGE));
		}
		//请求服务获取预览订单数据--start
		String serverTime = orderService.previewOrderTime();
		Future<String> futureServerTime = RpcContext.getContext().getFuture();
		CartAddressQueryDTO cartAddressQueryDTO = new CartAddressQueryDTO();
		cartAddressQueryDTO.setId(0);
		cartAddressQueryDTO.setUid(uid);
		CartAddressResultDTO address = addressServie.previewCVOrderAddress(cartAddressQueryDTO,sid);
		Future<CartAddressResultDTO> futureaddress = RpcContext.getContext().getFuture();
		Map<String, Object> orderDetail = orderService.previewCVOrder(uid, sid, type, extra,isselfmention);
		//请求服务获取预览订单数据--end
		//获取数据
		try {
			serverTime = futureServerTime.get();
		} catch (InterruptedException | ExecutionException e) {
		}
		try {
			address = futureaddress.get();
		} catch (InterruptedException | ExecutionException e) {
			address = null;
		}
		orderDetail.put("serverTime", serverTime);
		orderDetail.put("address", address);
		return GsonUtil.GsonString(ResultUtil.getSuccess(orderDetail));
	}
}