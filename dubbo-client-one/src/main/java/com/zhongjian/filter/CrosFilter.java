package com.zhongjian.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import com.zhongjian.common.GsonUtil;
import com.zhongjian.common.ResponseHandle;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.component.PropUtil;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.service.user.UserService;
import com.zhongjian.util.CheckSumBuilderUtil;

/**
 * Servlet Filter implementation class CrosFilter
 */
@WebFilter(value = "/*", asyncSupported = true)
public class CrosFilter implements Filter {

	private PropUtil propUtil = (PropUtil) SpringContextHolder.getBean(PropUtil.class);
	
//	private UserService userService = (UserService) SpringContextHolder.getBean(UserService.class);
    /**
     * Default constructor. 
     */
    public CrosFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (!propUtil.getIsDebug()) {
			//check sign
			String appKey = request.getParameter("appKey");
			String curTime = request.getParameter("curTime");
			String nonce = request.getParameter("nonce");
			String checkSum = request.getParameter("checkSum");
			String realAppKey = propUtil.getAppkey();
			String realCheckSum = CheckSumBuilderUtil.getCheckSum(propUtil.getAppSecret(), nonce, curTime);
			if (appKey == null || !appKey.equals(realAppKey)) {
				ResponseHandle.wrappedResponse(response, GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.NO_PERMISION)));
			}
			if( checkSum == null || !checkSum.equals(realCheckSum)) {
				ResponseHandle.wrappedResponse(response, GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.NO_PERMISION)));
			}
		}
		
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type");
		httpResponse.setHeader("Access-Control-Allow-Methods", "*");
		//add login_token jungle
//		if (request.getParameter("login_token")  != null) {
//			request.setAttribute("uid",userService.getUidByLoginToken(request.getParameter("login_token")));
//			}else {
//				request.setAttribute("uid", 0);
//			}
		request.setAttribute("uid",32716);
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
