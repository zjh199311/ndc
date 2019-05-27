package com.zhongjian.factory.route.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhongjian.dao.route.RouteParameterDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.factory.route.template.BaseRouteTemplate;
import com.zhongjian.route.RouteAgreeDTO;
import com.zhongjian.route.RouteBean;
import com.zhongjian.util.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

/**
 * @author 李冬冬
 */
@Service
public class BaseRoute extends BaseRouteTemplate {


    @Override
    public Object route(RouteParameterDTO routeParameterDTO) {

        //将执行注解的方法用线程执行然后放在map里. 从map中获取.
        Map<String, List<RouteBean>> map = RouteAgreeDTO.map;
        List<RouteBean> routeBeans = map.get(routeParameterDTO.getOprType());
        Object invoke = null;
        if (!CollectionUtils.isEmpty(routeBeans))

        {
            RouteBean routeBean = routeBeans.get(0);
            //获取此注解的接口方法
            Method method = routeBean.getMethod();
            //改方法的实现类
            Object o = routeBean.getO();
            String data = routeParameterDTO.getParamData();
            Parameter[] parameters = method.getParameters();
            Parameter parameter = parameters[0];
            try {
                //得到方法的参数
                Class clazz = parameter.getType();
                Object object = JSONObject.parseObject(data, clazz);
                if (null != object) {
                    //执行方法
                    invoke = method.invoke(o, object);
                } else {
                    LogUtil.info("对象为空||json解析对象为空", "object" + object);
                }

            } catch (Exception e) {
                LogUtil.info(e, "获取方法值异常");
            }
        }

        return invoke;
    }
}
