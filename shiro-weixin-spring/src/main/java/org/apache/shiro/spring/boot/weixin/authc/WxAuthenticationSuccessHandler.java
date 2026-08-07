package org.apache.shiro.spring.boot.weixin.authc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.authc.AuthenticationSuccessHandler;
import org.apache.shiro.biz.authz.principal.ShiroPrincipal;
import org.apache.shiro.biz.utils.SubjectUtils;
import org.apache.shiro.biz.web.servlet.http.HttpStatus;
import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.apache.shiro.spring.boot.utils.SubjectJwtUtils;
import org.apache.shiro.spring.boot.weixin.token.WxMaAuthenticationToken;
import org.apache.shiro.spring.boot.weixin.token.WxMpAuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSONObject;

@Slf4j
public class WxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


	private JwtPayloadRepository jwtPayloadRepository;
	private boolean checkExpiry = false;

	public WxAuthenticationSuccessHandler() {
	}

	public WxAuthenticationSuccessHandler(JwtPayloadRepository jwtPayloadRepository, boolean checkExpiry) {
		this.jwtPayloadRepository = jwtPayloadRepository;
		this.checkExpiry = checkExpiry;
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return SubjectUtils.isAssignableFrom(token.getClass(), WxMaAuthenticationToken.class, WxMpAuthenticationToken.class);
	}

	@Override
	public void onAuthenticationSuccess(AuthenticationToken token, ServletRequest request, ServletResponse response,
			Subject subject) {
		try {
			String tokenString = "";
			if (Objects.nonNull(subject.getPrincipal())
					&& ShiroPrincipal.class.isAssignableFrom(subject.getPrincipal().getClass())) {
				tokenString = getJwtPayloadRepository().issueJwt(token, subject);
			}
			Map<String, Object> tokenMap = SubjectJwtUtils.tokenMap(subject, tokenString);
			WebUtils.toHttp(response).setStatus(HttpStatus.SC_OK);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			JSONObject.writeJSONString(response.getWriter(), tokenMap);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE - 4;
	}

	public JwtPayloadRepository getJwtPayloadRepository() {
		return jwtPayloadRepository;
	}

	public void setJwtPayloadRepository(JwtPayloadRepository jwtPayloadRepository) {
		this.jwtPayloadRepository = jwtPayloadRepository;
	}

	public boolean isCheckExpiry() {
		return checkExpiry;
	}

	public void setCheckExpiry(boolean checkExpiry) {
		this.checkExpiry = checkExpiry;
	}

}
