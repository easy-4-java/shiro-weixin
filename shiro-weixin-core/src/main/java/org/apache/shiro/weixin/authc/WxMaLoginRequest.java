package org.apache.shiro.weixin.authc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WxMaLoginRequest {

	protected String jscode;
	protected String sessionKey;
	protected String unionid;
	protected String openid;
	protected String signature;
	protected String rawData;
	protected String encryptedData;
	protected String iv;
	protected String token;
	protected WxMaPhoneNumberInfo phoneNumberInfo;
	protected WxMaUserInfo userInfo;

	@JsonCreator
	public WxMaLoginRequest(@JsonProperty("jscode") String jscode,
			@JsonProperty("sessionKey") String sessionKey,
			@JsonProperty("unionid") String unionid,
			@JsonProperty("openid") String openid,
			@JsonProperty("signature") String signature,
			@JsonProperty("rawData") String rawData,
			@JsonProperty("encryptedData") String encryptedData,
			@JsonProperty("iv") String iv,
			@JsonProperty("token") String token) {
		this.jscode = jscode;
		this.sessionKey = sessionKey;
		this.unionid = unionid;
		this.openid = openid;
		this.signature = signature;
		this.rawData = rawData;
		this.encryptedData = encryptedData;
		this.iv = iv;
		this.token = token;
	}

	public String getJscode() {
		return jscode;
	}

	public void setJscode(String jscode) {
		this.jscode = jscode;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getEncryptedData() {
		return encryptedData;
	}

	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public WxMaPhoneNumberInfo getPhoneNumberInfo() {
		return phoneNumberInfo;
	}

	public void setPhoneNumberInfo(WxMaPhoneNumberInfo phoneNumberInfo) {
		this.phoneNumberInfo = phoneNumberInfo;
	}

	public WxMaUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(WxMaUserInfo userInfo) {
		this.userInfo = userInfo;
	}

}
