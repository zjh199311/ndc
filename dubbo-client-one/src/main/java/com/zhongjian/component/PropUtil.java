package com.zhongjian.component;

import com.zhongjian.component.PropComponent;

public class PropUtil {

	private PropComponent propComponent;

	public void setPropComponent(PropComponent propComponent) {
		this.propComponent = propComponent;
	}

	private void setProp() {
		setAppkey((String) propComponent.getMap().get("client.appkey"));
		setAppSecret((String) propComponent.getMap().get("client.appSecret"));
		setIsDebug(Boolean.valueOf((String) propComponent.getMap().get("client.debug")).booleanValue());
	}

	private String appkey;
	private String appSecret;
	private boolean isDebug;

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public boolean getIsDebug() {
		return isDebug;
	}

	public void setIsDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

}
