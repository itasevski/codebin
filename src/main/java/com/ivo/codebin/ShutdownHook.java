package com.ivo.codebin;

import com.ivo.codebin.service.CsrfTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
// @DisposableBean is an interface to be implemented by beans that want to release resources or execute logic on destruction.
// In this scenario, we want to back up/persist the user CSRF tokens upon app termination. In a real world scenario, that would mean backing up the
// CSRF tokens or any other data upon server crash.
public class ShutdownHook implements DisposableBean {
    private final CsrfTokenService csrfService;

    @Override
    public void destroy() throws Exception {
        this.csrfService.persistTokens();
    }
}
