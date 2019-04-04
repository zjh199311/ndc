package com.zhongjian.component;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PropUtil {

	@Autowired
	private PropComponent propComponent;
	
	@PostConstruct
	private void setProp() {
		setYxUrl((String)propComponent.getMap().get("yx.MsgUrl"));
		setYxAccid((String)propComponent.getMap().get("yx.accid"));
		setYxAppKey((String)propComponent.getMap().get("yx.Appkey"));
		setYxAppSecret((String)propComponent.getMap().get("yx.AppSecret"));
	}
	
    private String yxUrl;  
    private String yxAppKey = "";
    private String yxAppSecret = "";
    private String yxAccid = "";
	public String getYxUrl() {
		return yxUrl;
	}
	public void setYxUrl(String yxUrl) {
		this.yxUrl = yxUrl;
	}
	public String getYxAppKey() {
		return yxAppKey;
	}
	public void setYxAppKey(String yxAppKey) {
		this.yxAppKey = yxAppKey;
	}
	public String getYxAppSecret() {
		return yxAppSecret;
	}
	public void setYxAppSecret(String yxAppSecret) {
		this.yxAppSecret = yxAppSecret;
	}
	public String getYxAccid() {
		return yxAccid;
	}  
	public void setYxAccid(String yxAccid) {
		this.yxAccid = yxAccid;
	}
    
    
}
