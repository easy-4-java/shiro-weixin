package org.apache.shiro.weixin.token;

import org.apache.shiro.biz.authc.token.DefaultAuthenticationToken;
import org.apache.shiro.weixin.authc.WxMaLoginRequest;

@SuppressWarnings("serial")
public class WxMaAuthenticationToken extends DefaultAuthenticationToken {

	protected WxMaLoginRequest principal;

	public WxMaAuthenticationToken(WxMaLoginRequest request, String host) {
		this.principal = request;
		setHost(host);
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

}
