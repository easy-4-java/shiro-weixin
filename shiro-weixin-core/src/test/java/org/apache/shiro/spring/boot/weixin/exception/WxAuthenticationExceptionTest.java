package org.apache.shiro.spring.boot.weixin.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.authc.AuthenticationException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link WxAuthenticationException}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class WxAuthenticationExceptionTest {

    @Test
    void shouldExtendAuthenticationException() {
        assertThat(new WxAuthenticationException()).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void defaultConstructorShouldCreateException() {
        WxAuthenticationException ex = new WxAuthenticationException();
        assertThat(ex.getMessage()).isNull();
    }

    @Test
    void messageConstructorShouldPreserveMessage() {
        WxAuthenticationException ex = new WxAuthenticationException("auth failed");
        assertThat(ex.getMessage()).isEqualTo("auth failed");
    }

    @Test
    void causeConstructorShouldPreserveCause() {
        RuntimeException cause = new RuntimeException("root");
        WxAuthenticationException ex = new WxAuthenticationException(cause);
        assertThat(ex.getCause()).isSameAs(cause);
    }

    @Test
    void messageAndCauseConstructorShouldPreserveBoth() {
        RuntimeException cause = new RuntimeException("root");
        WxAuthenticationException ex = new WxAuthenticationException("msg", cause);
        assertThat(ex.getMessage()).isEqualTo("msg");
        assertThat(ex.getCause()).isSameAs(cause);
    }
}
