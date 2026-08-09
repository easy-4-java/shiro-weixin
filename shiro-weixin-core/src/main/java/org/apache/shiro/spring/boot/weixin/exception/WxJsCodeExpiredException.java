package org.apache.shiro.spring.boot.weixin.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Exception thrown when the WeChat JS-Code has expired.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
@SuppressWarnings("serial")
public class WxJsCodeExpiredException extends AuthenticationException {

	public WxJsCodeExpiredException() {
		super();
	}

	public WxJsCodeExpiredException(String message, Throwable cause) {
		super(message, cause);
	}

	public WxJsCodeExpiredException(String message) {
		super(message);
	}

	public WxJsCodeExpiredException(Throwable cause) {
		super(cause);
	}

}
