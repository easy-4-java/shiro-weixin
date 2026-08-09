package org.apache.shiro.spring.boot.weixin.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Exception thrown when the WeChat JS-Code is incorrect.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
@SuppressWarnings("serial")
public class WxJsCodeIncorrectException extends AuthenticationException {

	public WxJsCodeIncorrectException() {
		super();
	}

	public WxJsCodeIncorrectException(String message, Throwable cause) {
		super(message, cause);
	}

	public WxJsCodeIncorrectException(String message) {
		super(message);
	}

	public WxJsCodeIncorrectException(Throwable cause) {
		super(cause);
	}

}
