package com.zhongjian.service.route;

import com.zhongjian.dao.route.RouteParameterDTO;
import com.zhongjian.dto.common.ResultDTO;

/**
 * @author ldd
 */
public interface RouteService {

    /**
     * 路由
     *
     * @return
     */
    Object route(RouteParameterDTO routeParameterDTO);
}
