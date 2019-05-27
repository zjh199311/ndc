package com.zhongjian.service.route.impl;

import com.zhongjian.dao.route.RouteParameterDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.factory.route.RouteFactory;
import com.zhongjian.factory.route.template.BaseRouteTemplate;
import com.zhongjian.service.route.RouteService;
import com.zhongjian.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ldd
 */
@Service
public class RouteServiceImpl implements RouteService {


    @Resource
    RouteFactory routeFactory;

    @Override
    public Object route(RouteParameterDTO routeParameterDTO) {

        BaseRouteTemplate baseRouteTemplate = routeFactory.baseRouteTemplate("ROUTE");
        return baseRouteTemplate.route(routeParameterDTO);
    }
}
