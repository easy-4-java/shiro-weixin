package org.apache.shiro.weixin.realm;

import java.util.Objects;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.realm.AbstractAuthorizingRealm;
import org.apache.shiro.biz.realm.AuthorizingRealmListener;
import org.apache.shiro.weixin.authc.WxMaLoginRequest;
import org.apache.shiro.weixin.token.WxMaAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;

@Slf4j
public class WxMaAuthorizingRealm extends AbstractAuthorizingRealm {


	private final WxMaService wxMaService;

	public WxMaAuthorizingRealm(WxMaService wxMaService) {
		this.wxMaService = wxMaService;
	}

	@Override
	public Class<? extends AuthenticationToken> getAuthenticationTokenClass() {
		return WxMaAuthenticationToken.class;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		log.info("Handle authentication token {}.", token);
		AuthenticationException ex = null;
		AuthenticationInfo info = null;
		try {
			WxMaAuthenticationToken loginToken = (WxMaAuthenticationToken) token;
			WxMaLoginRequest loginRequest = (WxMaLoginRequest) loginToken.getPrincipal();
			if (StringUtils.hasText(loginRequest.getJscode())) {
				WxMaJscode2SessionResult sessionResult = getWxMaService().jsCode2SessionInfo(loginRequest.getJscode());
				if (Objects.nonNull(sessionResult)) {
					loginRequest.setOpenid(sessionResult.getOpenid());
					loginRequest.setUnionid(sessionResult.getUnionid());
					loginRequest.setSessionKey(sessionResult.getSessionKey());
				}
			}
			if (StringUtils.hasText(loginRequest.getSessionKey())
					&& StringUtils.hasText(loginRequest.getEncryptedData())
					&& StringUtils.hasText(loginRequest.getIv())) {
				try {
					WxMaPhoneNumberInfo phoneNumberInfo = getWxMaService().getUserService()
							.getPhoneNoInfo(loginRequest.getSessionKey(), loginRequest.getEncryptedData(), loginRequest.getIv());
					if (Objects.nonNull(phoneNumberInfo) && StringUtils.hasText(phoneNumberInfo.getPhoneNumber())) {
						loginRequest.setPhoneNumberInfo(phoneNumberInfo);
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			if (Objects.isNull(loginRequest.getUserInfo()) && StringUtils.hasText(loginRequest.getSessionKey())
					&& StringUtils.hasText(loginRequest.getEncryptedData())
					&& StringUtils.hasText(loginRequest.getIv())) {
				WxMaUserInfo userInfo = getWxMaService().getUserService()
						.getUserInfo(loginRequest.getSessionKey(), loginRequest.getEncryptedData(), loginRequest.getIv());
				if (Objects.nonNull(userInfo)) {
					loginRequest.setUserInfo(userInfo);
				}
			}
			info = getRepository().getAuthenticationInfo(loginToken);
		} catch (AuthenticationException e) {
			ex = e;
		} catch (Exception e) {
			ex = new AuthenticationException(e);
		}
		notifyListeners(token, ex, info);
		if (Objects.nonNull(ex)) {
			throw ex;
		}
		return info;
	}

	private void notifyListeners(AuthenticationToken token, AuthenticationException ex, AuthenticationInfo info) {
		if (Objects.nonNull(getRealmsListeners()) && !getRealmsListeners().isEmpty()) {
			for (AuthorizingRealmListener realmListener : getRealmsListeners()) {
				if (Objects.nonNull(ex) || Objects.isNull(info)) {
					realmListener.onFailure(this, token, ex);
				} else {
					realmListener.onSuccess(this, info);
				}
			}
		}
	}

	public WxMaService getWxMaService() {
		return wxMaService;
	}

}
