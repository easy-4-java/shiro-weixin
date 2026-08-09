package org.apache.shiro.spring.boot.weixin.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.authc.AuthenticationException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for WeChat JS-Code exception classes.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class WxJsCodeExceptionsTest {

    @Test
    void expiredShouldExtendAuthenticationException() {
        assertThat(new WxJsCodeExpiredException()).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void incorrectShouldExtendAuthenticationException() {
        assertThat(new WxJsCodeIncorrectException()).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void invalidShouldExtendAuthenticationException() {
        assertThat(new WxJsCodeInvalidException()).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void notFoundShouldExtendAuthenticationException() {
        assertThat(new WxJsCodeNotFoundException()).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void expiredMessageConstructorShouldPreserveMessage() {
        WxJsCodeExpiredException ex = new WxJsCodeExpiredException("expired");
        assertThat(ex.getMessage()).isEqualTo("expired");
    }

    @Test
    void incorrectMessageConstructorShouldPreserveMessage() {
        WxJsCodeIncorrectException ex = new WxJsCodeIncorrectException("incorrect");
        assertThat(ex.getMessage()).isEqualTo("incorrect");
    }

    @Test
    void invalidMessageConstructorShouldPreserveMessage() {
        WxJsCodeInvalidException ex = new WxJsCodeInvalidException("invalid");
        assertThat(ex.getMessage()).isEqualTo("invalid");
    }

    @Test
    void notFoundMessageConstructorShouldPreserveMessage() {
        WxJsCodeNotFoundException ex = new WxJsCodeNotFoundException("not found");
        assertThat(ex.getMessage()).isEqualTo("not found");
    }

    @Test
    void expiredCauseConstructorShouldPreserveCause() {
        RuntimeException cause = new RuntimeException("root");
        assertThat(new WxJsCodeExpiredException(cause).getCause()).isSameAs(cause);
    }

    @Test
    void incorrectCauseConstructorShouldPreserveCause() {
        RuntimeException cause = new RuntimeException("root");
        assertThat(new WxJsCodeIncorrectException(cause).getCause()).isSameAs(cause);
    }

    @Test
    void invalidCauseConstructorShouldPreserveCause() {
        RuntimeException cause = new RuntimeException("root");
        assertThat(new WxJsCodeInvalidException(cause).getCause()).isSameAs(cause);
    }

    @Test
    void notFoundCauseConstructorShouldPreserveCause() {
        RuntimeException cause = new RuntimeException("root");
        assertThat(new WxJsCodeNotFoundException(cause).getCause()).isSameAs(cause);
    }
}
