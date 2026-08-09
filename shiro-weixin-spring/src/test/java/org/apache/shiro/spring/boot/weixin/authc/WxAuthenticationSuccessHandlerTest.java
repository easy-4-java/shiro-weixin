package org.apache.shiro.spring.boot.weixin.authc;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.biz.authc.AuthenticationSuccessHandler;
import org.apache.shiro.spring.boot.jwt.JwtPayloadRepository;
import org.apache.shiro.spring.boot.weixin.token.WxMaAuthenticationToken;
import org.apache.shiro.spring.boot.weixin.token.WxMpAuthenticationToken;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link WxAuthenticationSuccessHandler}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class WxAuthenticationSuccessHandlerTest {

    @Test
    void shouldImplementAuthenticationSuccessHandler() {
        WxAuthenticationSuccessHandler handler = new WxAuthenticationSuccessHandler();
        assertThat(handler).isInstanceOf(AuthenticationSuccessHandler.class);
    }

    @Test
    void defaultConstructorShouldSetDefaults() {
        WxAuthenticationSuccessHandler handler = new WxAuthenticationSuccessHandler();
        assertThat(handler.getJwtPayloadRepository()).isNull();
        assertThat(handler.isCheckExpiry()).isFalse();
    }

    @Test
    void parameterizedConstructorShouldSetFields() {
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        WxAuthenticationSuccessHandler handler = new WxAuthenticationSuccessHandler(repo, true);
        assertThat(handler.getJwtPayloadRepository()).isSameAs(repo);
        assertThat(handler.isCheckExpiry()).isTrue();
    }

    @Test
    void orderShouldBeMaxValueMinusFour() {
        WxAuthenticationSuccessHandler handler = new WxAuthenticationSuccessHandler();
        assertThat(handler.getOrder()).isEqualTo(Integer.MAX_VALUE - 4);
    }

    @Test
    void supportsShouldReturnTrueForWxMaToken() {
        WxAuthenticationSuccessHandler handler = new WxAuthenticationSuccessHandler();
        org.apache.shiro.spring.boot.weixin.authc.WxMaLoginRequest req =
                new org.apache.shiro.spring.boot.weixin.authc.WxMaLoginRequest("a", "b", "c", "d", "e", "f", "g", "h", "i");
        WxMaAuthenticationToken token = new WxMaAuthenticationToken(req, "host");
        assertThat(handler.supports(token)).isTrue();
    }

    @Test
    void supportsShouldReturnTrueForWxMpToken() {
        WxAuthenticationSuccessHandler handler = new WxAuthenticationSuccessHandler();
        org.apache.shiro.spring.boot.weixin.authc.WxMpLoginRequest req =
                new org.apache.shiro.spring.boot.weixin.authc.WxMpLoginRequest("code", "state", "token");
        WxMpAuthenticationToken token = new WxMpAuthenticationToken(req, "host");
        assertThat(handler.supports(token)).isTrue();
    }

    @Test
    void settersShouldUpdateFields() {
        WxAuthenticationSuccessHandler handler = new WxAuthenticationSuccessHandler();
        JwtPayloadRepository repo = new JwtPayloadRepository() {};
        handler.setJwtPayloadRepository(repo);
        handler.setCheckExpiry(true);
        assertThat(handler.getJwtPayloadRepository()).isSameAs(repo);
        assertThat(handler.isCheckExpiry()).isTrue();
    }
}
