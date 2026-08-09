package org.apache.shiro.spring.boot.weixin.token;

import org.apache.shiro.biz.authc.token.DefaultAuthenticationToken;
import org.apache.shiro.spring.boot.weixin.authc.WxMaLoginRequest;

/**
 * Authentication token for WeChat Mini Program login, carrying the {@link WxMaLoginRequest}
 * as the principal.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
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
