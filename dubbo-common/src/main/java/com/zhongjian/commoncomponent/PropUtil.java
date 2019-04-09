package com.zhongjian.commoncomponent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PropUtil {

	@Autowired
	private PropComponent propComponent;
	
	@PostConstruct
	private void setProp() {
		setYxUrl((String)propComponent.getMap().get("user.yx.msgurl"));
		setYxAccid((String)propComponent.getMap().get("user.yx.accid"));
		setYxAppKey((String)propComponent.getMap().get("user.yx.appkey"));
		setYxAppSecret((String)propComponent.getMap().get("user.yx.appsecret"));
		
		setAliAppId((String)propComponent.getMap().get("pay.ali.appid"));
		setAliBusinessId((String)propComponent.getMap().get("pay.ali.businessid"));
		setAliRSAPrivateKey((String)propComponent.getMap().get("pay.ali.rsaprivatekey"));
		setAliPayPublicKey((String)propComponent.getMap().get("pay.ali.paypublickey"));
		setAliNotifyUrl((String)propComponent.getMap().get("pay.ali.notifyurl"));
		setAliUrl((String)propComponent.getMap().get("pay.ali.url"));
		setAliCharset((String)propComponent.getMap().get("pay.ali.charset"));
		setAliFormat((String)propComponent.getMap().get("pay.ali.format"));
		setAliSignType((String)propComponent.getMap().get("pay.ali.signtype"));
	}
	
    private String yxUrl;  
    private String yxAppKey;
    private String yxAppSecret;
    private String yxAccid;
    
    private String aliAppId;
    private String aliBusinessId;
    private String aliRSAPrivateKey;
    private String aliPayPublicKey;
    private String aliNotifyUrl;
    private String aliUrl;
    private String aliCharset;
    private String aliFormat;
    private String aliSignType;

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
	public String getAliAppId() {
		return aliAppId;
	}
	public void setAliAppId(String aliAppId) {
		this.aliAppId = aliAppId;
	}
	public String getAliBusinessId() {
		return aliBusinessId;
	}
	public void setAliBusinessId(String aliBusinessId) {
		this.aliBusinessId = aliBusinessId;
	}
	public String getAliRSAPrivateKey() {
		return aliRSAPrivateKey;
	}
	public void setAliRSAPrivateKey(String aliRSAPrivateKey) {
		this.aliRSAPrivateKey = aliRSAPrivateKey;
	}
	public String getAliPayPublicKey() {
		return aliPayPublicKey;
	}
	public void setAliPayPublicKey(String aliPayPublicKey) {
		this.aliPayPublicKey = aliPayPublicKey;
	}
	public String getAliNotifyUrl() {
		return aliNotifyUrl;
	}
	public void setAliNotifyUrl(String aliNotifyUrl) {
		this.aliNotifyUrl = aliNotifyUrl;
	}
	public String getAliUrl() {
		return aliUrl;
	}
	public void setAliUrl(String aliUrl) {
		this.aliUrl = aliUrl;
	}
	public String getAliCharset() {
		return aliCharset;
	}
	public void setAliCharset(String aliCharset) {
		this.aliCharset = aliCharset;
	}
	public String getAliFormat() {
		return aliFormat;
	}
	public void setAliFormat(String aliFormat) {
		this.aliFormat = aliFormat;
	}
	public String getAliSignType() {
		return aliSignType;
	}
	public void setAliSignType(String aliSignType) {
		this.aliSignType = aliSignType;
	}
    
    
}
