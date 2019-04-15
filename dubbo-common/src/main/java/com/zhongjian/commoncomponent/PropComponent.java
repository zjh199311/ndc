package com.zhongjian.commoncomponent;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;


@Component
public class PropComponent {

	private HashMap<String, Object> map = new HashMap<>();
	
	@PostConstruct
	public void readProperties() throws IOException {
		 Properties properties = new Properties();
//		 FileInputStream in = new FileInputStream("/serverconfig.properties");
//		 properties.load(in);
	        properties.load(PropComponent.class.getClassLoader().getResourceAsStream("serverconfig.properties"));
	        Set<Entry<Object,Object>> entrySet = properties.entrySet();
	        for (Entry<Object, Object> entry : entrySet) {
	        	map.put((String)entry.getKey(), entry.getValue());
	        }
	}

	public HashMap<String, Object> getMap() {
		return map;
	}
}
