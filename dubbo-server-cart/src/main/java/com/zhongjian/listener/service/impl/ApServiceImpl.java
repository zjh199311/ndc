package com.zhongjian.listener.service.impl;

import com.zhongjian.aspect.annotation.MethodRoute;
import com.zhongjian.aspect.annotation.Route;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.listener.service.ApService;
import com.zhongjian.route.RouteAgreeDTO;
import com.zhongjian.route.RouteBean;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: ldd
 */
@Service
public class ApServiceImpl implements ApService {
    @Override
    public void start() {
//        ExecutorService pool = Executors.newCachedThreadPool();
//        pool.execute(new RouteLoader());
    }

    private class RouteLoader extends Thread {
        public RouteLoader() {
            super();
        }

        @Override
        public void run() {
            Reflections reflections = new Reflections("com.zhongjian.service");
            Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Route.class);
            // 存放url和ExecutorBean的对应关系
            for (Class<?> aClass : typesAnnotatedWith) {
                //得到该类下面的所有方法
                Method[] methods = aClass.getDeclaredMethods();
                for (Method method : methods) {
                    //得到该类下面的RequestMapping注解
                    if (method.isAnnotationPresent(MethodRoute.class)) {
                        MethodRoute methodRoute = method.getAnnotation(MethodRoute.class);
                        AnnotatedType[] annotatedTypes = aClass.getAnnotatedInterfaces();
                        if (annotatedTypes.length == 0 && null != methodRoute) {
                            Object o = SpringContextHolder.getBean(aClass);

                            RouteBean routeBean = new RouteBean();
                            routeBean.setO(o);
                            routeBean.setMethod(method);
                            if (methodRoute.className() != null && methodRoute.className().length > 0) {
                                routeBean.setList(Arrays.asList(methodRoute.className()));
                            }
                            if (RouteAgreeDTO.map.containsKey(methodRoute.value())) {
                                RouteAgreeDTO.map.get(methodRoute.value()).add(routeBean);
                            } else {
                                List<RouteBean> beans = new ArrayList<>();
                                beans.add(routeBean);
                                RouteAgreeDTO.map.put(methodRoute.value(), beans);
                            }
                        }
                    }
                }
            }
        }
    }
}

