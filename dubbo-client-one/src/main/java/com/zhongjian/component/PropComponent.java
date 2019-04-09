package com.zhongjian.component;

import java.io.IOException;
import java.util.Map.Entry;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class PropComponent {

	private HashMap<String, Object> map = new HashMap<>();
	
	public void readProperties() throws IOException {
		 Properties properties = new Properties();
	        properties.load(PropComponent.class.getResourceAsStream("/currentclient.properties"));
	        Set<Entry<Object,Object>> entrySet = properties.entrySet();
	        for (Entry<Object, Object> entry : entrySet) {
	        	map.put((String)entry.getKey(), entry.getValue());
	        }
	}

	public HashMap<String, Object> getMap() {
		return map;
	}
}
