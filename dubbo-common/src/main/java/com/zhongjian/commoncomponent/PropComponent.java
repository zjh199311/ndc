package com.zhongjian.commoncomponent;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.zhongjian.util.LogUtil;


@Component
public class PropComponent {

	private HashMap<String, Object> map = new HashMap<>();
	
	@PostConstruct
	public void readProperties() throws IOException {
		 Properties properties = new Properties();
		 FileInputStream in = new FileInputStream("serverconfig.properties");
		 properties.load(in);
	        Set<Entry<Object,Object>> entrySet = properties.entrySet();
	        for (Entry<Object, Object> entry : entrySet) {
	        	map.put((String)entry.getKey(), entry.getValue());
	        }
	        LogUtil.info("配置ip",(String)map.get("spring.dubbo.protocol.host"));
	        LogUtil.info("配置port",(String)map.get("spring.dubbo.protocol.port"));
	}

	public HashMap<String, Object> getMap() {
		return map;
	}
}
