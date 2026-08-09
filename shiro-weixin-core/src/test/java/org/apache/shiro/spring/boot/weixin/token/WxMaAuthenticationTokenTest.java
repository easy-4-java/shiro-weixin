package org.apache.shiro.spring.boot.weixin.token;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.biz.authc.token.DefaultAuthenticationToken;
import org.apache.shiro.spring.boot.weixin.authc.WxMaLoginRequest;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link WxMaAuthenticationToken}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class WxMaAuthenticationTokenTest {

    @Test
    void shouldExtendDefaultAuthenticationToken() {
        WxMaLoginRequest request = new WxMaLoginRequest("jscode", "", "", "", "", "", "", "", "");
        WxMaAuthenticationToken token = new WxMaAuthenticationToken(request, "127.0.0.1");
        assertThat(token).isInstanceOf(DefaultAuthenticationToken.class);
    }

    @Test
    void principalShouldReturnLoginRequest() {
        WxMaLoginRequest request = new WxMaLoginRequest("jscode", "", "", "", "", "", "", "", "");
        WxMaAuthenticationToken token = new WxMaAuthenticationToken(request, "host");
        assertThat(token.getPrincipal()).isSameAs(request);
    }

    @Test
    void hostShouldReturnConstructorValue() {
        WxMaLoginRequest request = new WxMaLoginRequest("jscode", "", "", "", "", "", "", "", "");
        WxMaAuthenticationToken token = new WxMaAuthenticationToken(request, "192.168.1.1");
        assertThat(token.getHost()).isEqualTo("192.168.1.1");
    }
}
