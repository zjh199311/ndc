package com.zhongjian.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletRequest;



public class FormDataUtil {
	public static Map<String, String> getFormData(ServletRequest request){
		Map<String, String[]> requestParams = request.getParameterMap();
		HashMap<String, String> paramsMap = new HashMap<>();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = iter.next();
			String[] values = requestParams.get(name);
			paramsMap.put(name, values[0]);
		}
		return paramsMap;
	}
	
}
