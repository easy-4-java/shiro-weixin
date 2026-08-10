package org.apache.shiro.weixin.token;

import org.apache.shiro.biz.authc.token.DefaultAuthenticationToken;
import org.apache.shiro.weixin.authc.WxMpLoginRequest;

@SuppressWarnings("serial")
public class WxMpAuthenticationToken extends DefaultAuthenticationToken {

	protected WxMpLoginRequest principal;

	public WxMpAuthenticationToken(WxMpLoginRequest request, String host) {
		this.principal = request;
		setHost(host);
	}

	@Override
	public WxMpLoginRequest getPrincipal() {
		return principal;
	}

}
