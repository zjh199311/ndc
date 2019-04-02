package com.zhongjian.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ASUS on 2017/1/17.
 */
public class MapUtil {

    private static final MapUtil INSTANCE = new MapUtil();

    private MapUtil() {}

    public static MapUtil getInstance() {
        return INSTANCE;
    }

    public static  Map<String, String> parseObjectToMap(Object protocol) throws IntrospectionException, IllegalAccessException,
            InvocationTargetException, UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        if (protocol == null) {
            return null;
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(protocol.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (!"class".equals(key)) {
                Method getter = property.getReadMethod();
                Object value = getter.invoke(protocol);
                if (null != value) {
                    map.put(key, String.valueOf(value));
                }
            }
        }
        return map;
    }

    public static Map<String, String> parseObjectToMapIgnoreEmpty(Object protocol) throws IntrospectionException, IllegalAccessException,
            InvocationTargetException, UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        if (protocol == null) {
            return null;
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(protocol.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (!"class".equals(key)) {
                Method getter = property.getReadMethod();
                Object value = getter.invoke(protocol);
                if (null != value && StringUtil.isNotBlank(String.valueOf(value)) ) {
                    map.put(key, String.valueOf(value));
                }
            }
        }
        return map;
    }

    public static TreeMap parseObjectToTreeMap(Object protocol)throws IntrospectionException, InvocationTargetException, IllegalAccessException
    {
        TreeMap treeMap = new TreeMap();
        if (protocol == null) {
            return null;
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(protocol.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (!"class".equals(key)) {
                Method getter = property.getReadMethod();
                Object value = getter.invoke(protocol);
                if (null != value) {
                    treeMap.put(key, String.valueOf(value));
                }
            }
        }
        return treeMap;
    }

    public static HashMap parseObjectToHashMap(Object protocol) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        HashMap hashMap = new HashMap();
        if (protocol == null) {
            return null;
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(protocol.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (!"class".equals(key)) {
                Method getter = property.getReadMethod();
                Object value = getter.invoke(protocol);
                if (null != value) {
                    hashMap.put(key, String.valueOf(value));
                }
            }
        }
        return hashMap;
    }

}
