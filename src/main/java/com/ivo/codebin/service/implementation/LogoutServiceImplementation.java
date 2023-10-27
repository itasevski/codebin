package com.ivo.codebin.service.implementation;

import com.ivo.codebin.model.JwtToken;
import com.ivo.codebin.model.exception.AbsentCookieException;
import com.ivo.codebin.service.CookieService;
import com.ivo.codebin.service.CsrfTokenService;
import com.ivo.codebin.service.JwtTokenService;
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

    private final JwtTokenService jwtService;
    private final CookieService cookieService;
    private final CsrfTokenService csrfService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String accessToken;
        final String username;

        if (request.getCookies() == null || !this.cookieService.authCookieExists(request))
            throw new AbsentCookieException("Required Auth Cookie is not present!");
        // when adding our custom logout service to Spring Security, our AuthFilter (the OncePerRequestFilter) isn't executed at all,
        // so we must check the presence of the cookies here.

        accessToken = this.cookieService.extractCookie(request, "access-token");

        username = this.jwtService.extractUsername(accessToken);

        List<JwtToken> tokens = this.jwtService.findValidUserTokens(username);
        tokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
            this.jwtService.save(token);
            // we invalidate both the access and refresh tokens and save them in our database, after which the logout success handler is invoked and the context is cleared.
        });

        this.csrfService.removeToken(username);
        // we also remove the current CSRF token for the user.
    }
}
