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
		setAliCharset((String) propComponent.getMap().get("client.pay.ali.charset"));
		setAliSigntype((String) propComponent.getMap().get("client.pay.ali.signtype"));
		setAliPublicKey((String) propComponent.getMap().get("client.pay.ali.paypublickey"));
		setAliAppid((String) propComponent.getMap().get("client.pay.ali.appid"));
		setWxAppKey((String) propComponent.getMap().get("client.pay.wxApp.key"));
		setWxAppletKey((String) propComponent.getMap().get("client.pay.wxApp.appletsKey"));
	}

	private String appkey;
	private String appSecret;
	private boolean isDebug;
	private String aliCharset;
	private String aliSigntype;
	private String aliPublicKey;
	private String aliAppid;
	private String wxAppKey;
	private String wxAppletKey;
	
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

	public String getAliCharset() {
		return aliCharset;
	}

	public void setAliCharset(String aliCharset) {
		this.aliCharset = aliCharset;
	}

	public String getAliSigntype() {
		return aliSigntype;
	}

	public void setAliSigntype(String aliSigntype) {
		this.aliSigntype = aliSigntype;
	}

	public String getAliPublicKey() {
		return aliPublicKey;
	}

	public void setAliPublicKey(String aliPublicKey) {
		this.aliPublicKey = aliPublicKey;
	}

	public String getAliAppid() {
		return aliAppid;
	}

	public void setAliAppid(String aliAppid) {
		this.aliAppid = aliAppid;
	}

	public String getWxAppKey() {
		return wxAppKey;
	}

	public void setWxAppKey(String wxAppKey) {
		this.wxAppKey = wxAppKey;
	}

	public String getWxAppletKey() {
		return wxAppletKey;
	}

	public void setWxAppletKey(String wxAppletKey) {
		this.wxAppletKey = wxAppletKey;
	}

	
}
