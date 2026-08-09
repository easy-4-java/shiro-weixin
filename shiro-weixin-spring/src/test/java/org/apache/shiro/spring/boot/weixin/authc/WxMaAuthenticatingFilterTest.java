package org.apache.shiro.spring.boot.weixin.authc;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.biz.web.filter.authc.AbstractTrustableAuthenticatingFilter;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link WxMaAuthenticatingFilter}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class WxMaAuthenticatingFilterTest {

    @Test
    void shouldExtendAbstractTrustableAuthenticatingFilter() {
        WxMaAuthenticatingFilter filter = new WxMaAuthenticatingFilter();
        assertThat(filter).isInstanceOf(AbstractTrustableAuthenticatingFilter.class);
    }

    @Test
    void defaultParameterNamesShouldBeSet() {
        WxMaAuthenticatingFilter filter = new WxMaAuthenticatingFilter();
        assertThat(filter.getJscodeParameter()).isEqualTo("jscode");
        assertThat(filter.getSignatureParameter()).isEqualTo("signature");
        assertThat(filter.getRawDataParameter()).isEqualTo("rawData");
        assertThat(filter.getEncryptedDataParameter()).isEqualTo("encryptedData");
        assertThat(filter.getIvParameter()).isEqualTo("iv");
        assertThat(filter.getTokenParameter()).isEqualTo("token");
    }

    @Test
    void settersShouldUpdateParameterNames() {
        WxMaAuthenticatingFilter filter = new WxMaAuthenticatingFilter();
        filter.setJscodeParameter("custom_jscode");
        filter.setSignatureParameter("custom_sig");
        filter.setRawDataParameter("custom_raw");
        filter.setEncryptedDataParameter("custom_enc");
        filter.setIvParameter("custom_iv");
        filter.setTokenParameter("custom_token");
        assertThat(filter.getJscodeParameter()).isEqualTo("custom_jscode");
        assertThat(filter.getSignatureParameter()).isEqualTo("custom_sig");
        assertThat(filter.getRawDataParameter()).isEqualTo("custom_raw");
        assertThat(filter.getEncryptedDataParameter()).isEqualTo("custom_enc");
        assertThat(filter.getIvParameter()).isEqualTo("custom_iv");
        assertThat(filter.getTokenParameter()).isEqualTo("custom_token");
    }
}
