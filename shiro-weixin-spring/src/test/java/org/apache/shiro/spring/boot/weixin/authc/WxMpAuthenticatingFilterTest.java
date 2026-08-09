package org.apache.shiro.spring.boot.weixin.authc;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.biz.web.filter.authc.AbstractTrustableAuthenticatingFilter;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link WxMpAuthenticatingFilter}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class WxMpAuthenticatingFilterTest {

    @Test
    void shouldExtendAbstractTrustableAuthenticatingFilter() {
        WxMpAuthenticatingFilter filter = new WxMpAuthenticatingFilter();
        assertThat(filter).isInstanceOf(AbstractTrustableAuthenticatingFilter.class);
    }

    @Test
    void defaultParameterNamesShouldBeSet() {
        WxMpAuthenticatingFilter filter = new WxMpAuthenticatingFilter();
        assertThat(filter.getCodeParameter()).isEqualTo("code");
        assertThat(filter.getTokenParameter()).isEqualTo("token");
    }

    @Test
    void settersShouldUpdateParameterNames() {
        WxMpAuthenticatingFilter filter = new WxMpAuthenticatingFilter();
        filter.setCodeParameter("custom_code");
        filter.setTokenParameter("custom_token");
        assertThat(filter.getCodeParameter()).isEqualTo("custom_code");
        assertThat(filter.getTokenParameter()).isEqualTo("custom_token");
    }
}
