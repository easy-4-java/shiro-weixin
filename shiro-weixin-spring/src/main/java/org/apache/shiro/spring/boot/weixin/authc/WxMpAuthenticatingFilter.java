package org.apache.shiro.spring.boot.weixin.authc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.authc.AuthcResponse;
import org.apache.shiro.biz.utils.WebUtils2;
import org.apache.shiro.biz.web.filter.authc.AbstractTrustableAuthenticatingFilter;
import org.apache.shiro.biz.web.servlet.http.HttpStatus;
import org.apache.shiro.spring.boot.weixin.token.WxMpAuthenticationToken;
import org.apache.shiro.web.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSONObject;

@Slf4j
public class WxMpAuthenticatingFilter extends AbstractTrustableAuthenticatingFilter {


	public static final String SPRING_SECURITY_FORM_CODE_KEY = "code";
	public static final String SPRING_SECURITY_FORM_STATE_KEY = "state";
	public static final String SPRING_SECURITY_FORM_TOKEN_KEY = "token";

	private String codeParameter = SPRING_SECURITY_FORM_CODE_KEY;
	private String stateParameter = SPRING_SECURITY_FORM_STATE_KEY;
	private String tokenParameter = SPRING_SECURITY_FORM_TOKEN_KEY;

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				if (log.isTraceEnabled()) {
					log.trace("Login submission detected. Attempting to execute login.");
				}
				return executeLogin(request, response);
			}
			String message = "Authentication url [" + getLoginUrl() + "] Not Http Post request.";
			if (log.isTraceEnabled()) {
				log.trace(message);
			}
			writeFailure(response, HttpStatus.SC_BAD_REQUEST, message);
			return false;
		}
		String message = "Attempting to access a path which requires authentication.";
		if (log.isTraceEnabled()) {
			log.trace(message);
		}
		if (WebUtils2.isAjaxRequest(request)) {
			writeFailure(response, HttpStatus.SC_UNAUTHORIZED, message);
			return false;
		}
		saveRequestAndRedirectToLogin(request, response);
		return false;
	}

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		if (WebUtils2.isObjectRequest(request)) {
			try {
				WxMpLoginRequest loginRequest = objectMapper.readValue(request.getReader(), WxMpLoginRequest.class);
				return new WxMpAuthenticationToken(loginRequest, getHost(request));
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		WxMpLoginRequest loginRequest = new WxMpLoginRequest(valueOrEmpty(obtainCode(request)),
				valueOrEmpty(obtainState(request)), valueOrEmpty(obtainToken(request)));
		return new WxMpAuthenticationToken(loginRequest, getHost(request));
	}

	protected String obtainCode(ServletRequest request) {
		return request.getParameter(codeParameter);
	}

	protected String obtainState(ServletRequest request) {
		return request.getParameter(stateParameter);
	}

	protected String obtainToken(ServletRequest request) {
		return request.getParameter(tokenParameter);
	}

	private String valueOrEmpty(String value) {
		return Objects.isNull(value) ? "" : value;
	}

	private void writeFailure(ServletResponse response, int status, String message) throws IOException {
		WebUtils.toHttp(response).setStatus(HttpStatus.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		JSONObject.writeJSONString(response.getWriter(), AuthcResponse.fail(status, message));
	}

	public String getCodeParameter() {
		return codeParameter;
	}

	public void setCodeParameter(String codeParameter) {
		this.codeParameter = codeParameter;
	}

	public String getTokenParameter() {
		return tokenParameter;
	}

	public void setTokenParameter(String tokenParameter) {
		this.tokenParameter = tokenParameter;
	}

}
