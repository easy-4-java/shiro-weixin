package org.apache.shiro.spring.boot.weixin.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Exception thrown when the WeChat JS-Code is invalid.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
@SuppressWarnings("serial")
public class WxJsCodeInvalidException extends AuthenticationException {

	public WxJsCodeInvalidException() {
		super();
	}

	public WxJsCodeInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

	public WxJsCodeInvalidException(String message) {
		super(message);
	}

	public WxJsCodeInvalidException(Throwable cause) {
		super(cause);
	}

}
