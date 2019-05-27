package com.zhongjian.factory.route.template;

import com.zhongjian.dao.route.RouteParameterDTO;
import com.zhongjian.dto.common.ResultDTO;

/**
 * @author 李冬冬
 */
public abstract class BaseRouteTemplate {

    /**
     * 处理路由
     */
    public abstract Object route(RouteParameterDTO routeParameterDTO);
}
