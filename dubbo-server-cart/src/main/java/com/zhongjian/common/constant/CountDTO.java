package com.zhongjian.common.constant;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ldd
 */
public class CountDTO {

    public static Map<String,Integer> map = new ConcurrentHashMap<>(10000);
}
