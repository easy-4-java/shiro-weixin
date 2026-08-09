package org.apache.shiro.spring.boot.weixin;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.biz.authz.principal.ShiroPrincipal;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ShiroWeiXinPrincipal}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class ShiroWeiXinPrincipalTest {

    @Test
    void shouldExtendShiroPrincipal() {
        ShiroWeiXinPrincipal principal = new ShiroWeiXinPrincipal();
        assertThat(principal).isInstanceOf(ShiroPrincipal.class);
    }
}
