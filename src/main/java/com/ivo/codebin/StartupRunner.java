package com.ivo.codebin;

import com.ivo.codebin.service.CsrfTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// This class is used to run some business logic when the application starts up. For that purpose, we must implement the ApplicationRunner interface and
// annotate this class as a @Component, so that Spring can register it as a bean.
// In this case, we want to load the backed up CSRF tokens from database into memory.
@Slf4j
@Component
@RequiredArgsConstructor
public class StartupRunner implements ApplicationRunner {
    private final CsrfTokenService csrfService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Restoring backed up CSRF tokens from database...");
        this.csrfService.loadTokens();
        log.info("Cleaning up database...");
        this.csrfService.deleteTokens();
    }
}
