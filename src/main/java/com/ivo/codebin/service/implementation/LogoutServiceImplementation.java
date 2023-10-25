package com.ivo.codebin.service.implementation;

import com.ivo.codebin.model.Token;
import com.ivo.codebin.model.exception.AbsentCookieException;
import com.ivo.codebin.repository.TokenRepository;
import com.ivo.codebin.service.CookieService;
import com.ivo.codebin.service.CsrfService;
import com.ivo.codebin.service.JwtService;
import com.ivo.codebin.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LogoutServiceImplementation implements LogoutService {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final CsrfService csrfService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String jwt;
        final String username;

        if (request.getCookies() == null || !this.cookieService.authCookieExists(request))
            throw new AbsentCookieException("Required Auth Cookie is not present!");
        // when adding our custom logout service to Spring Security, our AuthFilter (the OncePerRequestFilter) isn't executed at all,
        // so we must check the presence of the cookies here.

        jwt = this.cookieService.extractCookie(request, "access-token");

        username = this.jwtService.extractUsername(jwt);

        List<Token> tokens = this.tokenRepository.findValidUserTokens(username);
        tokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
            this.tokenRepository.save(token);
            // we invalidate both the access and refresh tokens and save them in our database, after which the logout success handler is invoked and the context is cleared
            // The AuthFilter then gets executed and the token gets checked for its validity. The check fails and the other filters get invoked and executed normally.
        });

        this.csrfService.removeCsrfToken(username);
    }
}
