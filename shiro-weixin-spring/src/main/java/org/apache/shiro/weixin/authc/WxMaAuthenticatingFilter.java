package org.apache.shiro.weixin.authc;

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
import org.apache.shiro.weixin.exception.WxJsCodeInvalidException;
import org.apache.shiro.weixin.token.WxMaAuthenticationToken;
import org.apache.shiro.web.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

@Slf4j
public class WxMaAuthenticatingFilter extends AbstractTrustableAuthenticatingFilter {


	public static final String SPRING_SECURITY_FORM_JSCODE_KEY = "jscode";
	public static final String SPRING_SECURITY_FORM_SESSIONKEY_KEY = "sessionKey";
	public static final String SPRING_SECURITY_FORM_UNIONID_KEY = "unionid";
	public static final String SPRING_SECURITY_FORM_OPENID_KEY = "openid";
	public static final String SPRING_SECURITY_FORM_SIGNATURE_KEY = "signature";
	public static final String SPRING_SECURITY_FORM_RAWDATA_KEY = "rawData";
	public static final String SPRING_SECURITY_FORM_ENCRYPTEDDATA_KEY = "encryptedData";
	public static final String SPRING_SECURITY_FORM_IV_KEY = "iv";
	public static final String SPRING_SECURITY_FORM_TOKEN_KEY = "token";

	private String jscodeParameter = SPRING_SECURITY_FORM_JSCODE_KEY;
	private String sessionKeyParameter = SPRING_SECURITY_FORM_SESSIONKEY_KEY;
	private String unionidParameter = SPRING_SECURITY_FORM_UNIONID_KEY;
	private String openidParameter = SPRING_SECURITY_FORM_OPENID_KEY;
	private String signatureParameter = SPRING_SECURITY_FORM_SIGNATURE_KEY;
	private String rawDataParameter = SPRING_SECURITY_FORM_RAWDATA_KEY;
	private String encryptedDataParameter = SPRING_SECURITY_FORM_ENCRYPTEDDATA_KEY;
	private String ivParameter = SPRING_SECURITY_FORM_IV_KEY;
	private String tokenParameter = SPRING_SECURITY_FORM_TOKEN_KEY;

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
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

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		if (WebUtils2.isObjectRequest(request)) {
			try {
				WxMaLoginRequest loginRequest = objectMapper.readValue(request.getReader(), WxMaLoginRequest.class);
				if (!StringUtils.hasText(loginRequest.getJscode())) {
					log.debug("No jscode found in request.");
					throw new WxJsCodeInvalidException("No jscode found in request.");
				}
				return new WxMaAuthenticationToken(loginRequest, getHost(request));
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		String jscode = obtainJscode(request);
		if (!StringUtils.hasText(jscode)) {
			log.debug("No jscode found in request.");
			throw new WxJsCodeInvalidException("No jscode found in request.");
		}
		WxMaLoginRequest loginRequest = new WxMaLoginRequest(jscode, valueOrEmpty(obtainSessionKey(request)),
				valueOrEmpty(obtainUnionid(request)), valueOrEmpty(obtainOpenid(request)),
				valueOrEmpty(obtainSignature(request)), valueOrEmpty(obtainRawData(request)),
				valueOrEmpty(obtainEncryptedData(request)), valueOrEmpty(obtainIv(request)),
				valueOrEmpty(obtainToken(request)));
		return new WxMaAuthenticationToken(loginRequest, getHost(request));
	}

	protected String obtainJscode(ServletRequest request) {
		return request.getParameter(jscodeParameter);
	}

	protected String obtainSessionKey(ServletRequest request) {
		return request.getParameter(sessionKeyParameter);
	}

	protected String obtainUnionid(ServletRequest request) {
		return request.getParameter(unionidParameter);
	}

	protected String obtainOpenid(ServletRequest request) {
		return request.getParameter(openidParameter);
	}

	protected String obtainSignature(ServletRequest request) {
		return request.getParameter(signatureParameter);
	}

	protected String obtainRawData(ServletRequest request) {
		return request.getParameter(rawDataParameter);
	}

	protected String obtainEncryptedData(ServletRequest request) {
		return request.getParameter(encryptedDataParameter);
	}

	protected String obtainIv(ServletRequest request) {
		return request.getParameter(ivParameter);
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

	public String getJscodeParameter() {
		return jscodeParameter;
	}

	public void setJscodeParameter(String jscodeParameter) {
		this.jscodeParameter = jscodeParameter;
	}

	public String getSignatureParameter() {
		return signatureParameter;
	}

	public void setSignatureParameter(String signatureParameter) {
		this.signatureParameter = signatureParameter;
	}

	public String getRawDataParameter() {
		return rawDataParameter;
	}

	public void setRawDataParameter(String rawDataParameter) {
		this.rawDataParameter = rawDataParameter;
	}

	public String getEncryptedDataParameter() {
		return encryptedDataParameter;
	}

	public void setEncryptedDataParameter(String encryptedDataParameter) {
		this.encryptedDataParameter = encryptedDataParameter;
	}

	public String getIvParameter() {
		return ivParameter;
	}

	public void setIvParameter(String ivParameter) {
		this.ivParameter = ivParameter;
	}

	public String getTokenParameter() {
		return tokenParameter;
	}

	public void setTokenParameter(String tokenParameter) {
		this.tokenParameter = tokenParameter;
	}

}
