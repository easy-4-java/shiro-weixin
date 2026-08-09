package org.apache.shiro.spring.boot.weixin.token;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.biz.authc.token.DefaultAuthenticationToken;
import org.apache.shiro.spring.boot.weixin.authc.WxMpLoginRequest;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link WxMpAuthenticationToken}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class WxMpAuthenticationTokenTest {

    @Test
    void shouldExtendDefaultAuthenticationToken() {
        WxMpLoginRequest request = new WxMpLoginRequest("code", "state", "token");
        WxMpAuthenticationToken token = new WxMpAuthenticationToken(request, "127.0.0.1");
        assertThat(token).isInstanceOf(DefaultAuthenticationToken.class);
    }

    @Test
    void principalShouldReturnLoginRequest() {
        WxMpLoginRequest request = new WxMpLoginRequest("code", "state", "token");
        WxMpAuthenticationToken token = new WxMpAuthenticationToken(request, "host");
        assertThat(token.getPrincipal()).isSameAs(request);
    }

    @Test
    void hostShouldReturnConstructorValue() {
        WxMpLoginRequest request = new WxMpLoginRequest("code", "state", "token");
        WxMpAuthenticationToken token = new WxMpAuthenticationToken(request, "192.168.1.1");
        assertThat(token.getHost()).isEqualTo("192.168.1.1");
    }
}
