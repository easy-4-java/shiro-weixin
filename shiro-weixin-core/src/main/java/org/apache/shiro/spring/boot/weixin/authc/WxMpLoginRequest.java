package org.apache.shiro.spring.boot.weixin.authc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;

/**
 * Login request payload for WeChat Official Account (Mp) OAuth2 authentication.
 * Contains the authorization code, state, and user info.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxMpLoginRequest {

	protected String code;
	protected String token;
	protected String state;
	protected String unionid;
	protected String openid;
	protected String lang = "zh_CN";
	protected WxOAuth2AccessToken accessToken;
	protected WxOAuth2UserInfo userInfo;

	@JsonCreator
	public WxMpLoginRequest(@JsonProperty("code") String code,
			@JsonProperty("state") String state,
			@JsonProperty("token") String token) {
		this.code = code;
		this.state = state;
		this.token = token;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public WxOAuth2AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(WxOAuth2AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public WxOAuth2UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(WxOAuth2UserInfo userInfo) {
		this.userInfo = userInfo;
	}

}
