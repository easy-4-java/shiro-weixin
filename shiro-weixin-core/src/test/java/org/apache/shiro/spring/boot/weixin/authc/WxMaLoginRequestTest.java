package org.apache.shiro.spring.boot.weixin.authc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link WxMaLoginRequest}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class WxMaLoginRequestTest {

    @Test
    void constructorShouldSetAllFields() {
        WxMaLoginRequest req = new WxMaLoginRequest("jscode", "sessionKey", "unionid",
                "openid", "signature", "rawData", "encryptedData", "iv", "token");
        assertThat(req.getJscode()).isEqualTo("jscode");
        assertThat(req.getSessionKey()).isEqualTo("sessionKey");
        assertThat(req.getUnionid()).isEqualTo("unionid");
        assertThat(req.getOpenid()).isEqualTo("openid");
        assertThat(req.getSignature()).isEqualTo("signature");
        assertThat(req.getRawData()).isEqualTo("rawData");
        assertThat(req.getEncryptedData()).isEqualTo("encryptedData");
        assertThat(req.getIv()).isEqualTo("iv");
        assertThat(req.getToken()).isEqualTo("token");
    }

    @Test
    void settersShouldUpdateFields() {
        WxMaLoginRequest req = new WxMaLoginRequest("a", "b", "c", "d", "e", "f", "g", "h", "i");
        req.setJscode("newJscode");
        req.setSessionKey("newSession");
        req.setUnionid("newUnion");
        req.setOpenid("newOpenid");
        req.setSignature("newSig");
        req.setRawData("newRaw");
        req.setEncryptedData("newEnc");
        req.setIv("newIv");
        req.setToken("newToken");
        assertThat(req.getJscode()).isEqualTo("newJscode");
        assertThat(req.getSessionKey()).isEqualTo("newSession");
        assertThat(req.getUnionid()).isEqualTo("newUnion");
        assertThat(req.getOpenid()).isEqualTo("newOpenid");
        assertThat(req.getSignature()).isEqualTo("newSig");
        assertThat(req.getRawData()).isEqualTo("newRaw");
        assertThat(req.getEncryptedData()).isEqualTo("newEnc");
        assertThat(req.getIv()).isEqualTo("newIv");
        assertThat(req.getToken()).isEqualTo("newToken");
    }

    @Test
    void phoneNumberInfoShouldBeSettable() {
        WxMaLoginRequest req = new WxMaLoginRequest("a", "b", "c", "d", "e", "f", "g", "h", "i");
        assertThat(req.getPhoneNumberInfo()).isNull();
        req.setPhoneNumberInfo(null);
        assertThat(req.getPhoneNumberInfo()).isNull();
    }

    @Test
    void userInfoShouldBeSettable() {
        WxMaLoginRequest req = new WxMaLoginRequest("a", "b", "c", "d", "e", "f", "g", "h", "i");
        assertThat(req.getUserInfo()).isNull();
        req.setUserInfo(null);
        assertThat(req.getUserInfo()).isNull();
    }
}
