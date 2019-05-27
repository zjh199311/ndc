package com.zhongjian.factory.route;

import com.zhongjian.factory.route.impl.BaseRoute;
import com.zhongjian.factory.route.template.BaseRouteTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 李冬冬
 */
@Component
public class RouteFactory {

    @Resource
    BaseRoute baseRoute;

    public BaseRouteTemplate baseRouteTemplate(String channleName){
        switch (channleName){
            case "ROUTE":
                return baseRoute;
            default:
                return null;
        }
    }
}
