package com.zhongjian.route;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;


@Data
public class RouteBean {
    private Object o;

    private Method method;

    private List<Class> list;
}
