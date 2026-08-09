package org.apache.shiro.spring.boot.weixin.authc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link WxMpLoginRequest}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class WxMpLoginRequestTest {

    @Test
    void constructorShouldSetCodeStateToken() {
        WxMpLoginRequest req = new WxMpLoginRequest("code123", "state456", "token789");
        assertThat(req.getCode()).isEqualTo("code123");
        assertThat(req.getState()).isEqualTo("state456");
        assertThat(req.getToken()).isEqualTo("token789");
    }

    @Test
    void langShouldDefaultToZhCn() {
        WxMpLoginRequest req = new WxMpLoginRequest("code", "state", "token");
        assertThat(req.getLang()).isEqualTo("zh_CN");
    }

    @Test
    void settersShouldUpdateFields() {
        WxMpLoginRequest req = new WxMpLoginRequest("code", "state", "token");
        req.setCode("newCode");
        req.setState("newState");
        req.setToken("newToken");
        req.setUnionid("newUnion");
        req.setOpenid("newOpenid");
        req.setLang("en");
        assertThat(req.getCode()).isEqualTo("newCode");
        assertThat(req.getState()).isEqualTo("newState");
        assertThat(req.getToken()).isEqualTo("newToken");
        assertThat(req.getUnionid()).isEqualTo("newUnion");
        assertThat(req.getOpenid()).isEqualTo("newOpenid");
        assertThat(req.getLang()).isEqualTo("en");
    }

    @Test
    void accessTokenAndUserInfoShouldDefaultToNull() {
        WxMpLoginRequest req = new WxMpLoginRequest("code", "state", "token");
        assertThat(req.getAccessToken()).isNull();
        assertThat(req.getUserInfo()).isNull();
    }
}
