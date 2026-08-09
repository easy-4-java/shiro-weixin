package org.apache.shiro.spring.boot.weixin.token;

import org.apache.shiro.biz.authc.token.DefaultAuthenticationToken;
import org.apache.shiro.spring.boot.weixin.authc.WxMpLoginRequest;

/**
 * Authentication token for WeChat Official Account login, carrying the {@link WxMpLoginRequest}
 * as the principal.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
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
