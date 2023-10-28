package com.ivo.codebin;

import com.ivo.codebin.service.CsrfTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
// @DisposableBean is an interface to be implemented by beans that want to release resources or execute logic on destruction.
// In this scenario, we want to back up/persist the user CSRF tokens upon app termination. In a real world scenario, that would mean backing up the
// CSRF tokens or any other data upon server crash.
public class ShutdownHook implements DisposableBean {
    private final CsrfTokenService csrfService;

    @Override
    public void destroy() throws Exception {
        log.info("Backing up CSRF tokens...");
        this.csrfService.persistTokens();
    }
}
