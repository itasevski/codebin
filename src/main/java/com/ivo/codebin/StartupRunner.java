package com.ivo.codebin;

import com.ivo.codebin.model.User;
import com.ivo.codebin.repository.UserRepository;
import com.ivo.codebin.service.CsrfService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

// This class is used to run some business logic when the application starts up. For that purpose, we must implement the ApplicationRunner interface and
// annotate this class as a @Component, so that Spring can register it as a bean.
// This class should only exist when running locally and should never be set in production. It is used to generate a CSRF token for every user in our
// database. This is done because the ConcurrentHashMap that stores the user's CSRF tokens is deleted from memory once the application stops, meaning that
// the authenticated user won't be able to access resources since the CSRF token that will be provided won't exist on the app's next startup.
@Component
@RequiredArgsConstructor
@Profile("csrf-tokens-local-init") // only run this class when this profile is active (set in application.properties)
public class StartupRunner implements ApplicationRunner {
    private final UserRepository userRepository;
    private final CsrfService csrfService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> usernames = this.userRepository.findAll().stream().map(User::getUsername).collect(Collectors.toList());

        usernames.forEach(this.csrfService::generateCsrfTokenForUsername);

        System.out.println(this.csrfService.printCsrfTokens());
    }
}
