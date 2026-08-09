package org.apache.shiro.spring.boot.weixin.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.authc.AuthenticationException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link URIUnpermittedException}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class URIUnpermittedExceptionTest {

    @Test
    void shouldExtendAuthenticationException() {
        assertThat(new URIUnpermittedException()).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void defaultConstructorShouldCreateException() {
        URIUnpermittedException ex = new URIUnpermittedException();
        assertThat(ex.getMessage()).isNull();
    }

    @Test
    void messageConstructorShouldPreserveMessage() {
        URIUnpermittedException ex = new URIUnpermittedException("unpermitted");
        assertThat(ex.getMessage()).isEqualTo("unpermitted");
    }

    @Test
    void causeConstructorShouldPreserveCause() {
        RuntimeException cause = new RuntimeException("root");
        URIUnpermittedException ex = new URIUnpermittedException(cause);
        assertThat(ex.getCause()).isSameAs(cause);
    }

    @Test
    void messageAndCauseConstructorShouldPreserveBoth() {
        RuntimeException cause = new RuntimeException("root");
        URIUnpermittedException ex = new URIUnpermittedException("msg", cause);
        assertThat(ex.getMessage()).isEqualTo("msg");
        assertThat(ex.getCause()).isSameAs(cause);
    }
}
