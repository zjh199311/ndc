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

import com.zhongjian.dto.cart.basket.query.CartBasketListQueryDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.service.cart.basket.CartBasketService;
import org.apache.log4j.Logger;

import com.zhongjian.common.GsonUtil;
import com.zhongjian.common.ResponseHandle;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.executor.ThreadPoolExecutorSingle;

import java.io.IOException;

@WebServlet(value = "/v1/cart/getStoreList", asyncSupported = true)
public class GetCartListServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(GetCartListServlet.class);

    private CartBasketService hmBasketService = (CartBasketService) SpringContextHolder
            .getBean(CartBasketService.class);

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
                    String result = GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.SERVERERR));
                    try {
                        Integer uid = (Integer) request.getAttribute("uid");
                        ServletRequest request2 = asyncContext.getRequest();
                        Integer gid = Integer.valueOf(request2.getParameter("sid"));
                        result = GetCartListServlet.this.handle(uid, gid);
                        // 返回数据
                        ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
                    } catch (Exception e) {
                        try {
                            ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        log.error("fail cart/getStoreList : " + e.getMessage());
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

    private String handle(Integer uid, Integer sid) {
        if (uid == 0) {
            return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.USER_IS_NULL));
        }
        CartBasketListQueryDTO cartBasketListQueryDTO = new CartBasketListQueryDTO();
        cartBasketListQueryDTO.setUid(uid);
        cartBasketListQueryDTO.setSid(sid);
        return GsonUtil.GsonString(hmBasketService.queryList(cartBasketListQueryDTO));
    }
}