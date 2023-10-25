package com.ivo.codebin.service.implementation;

import com.ivo.codebin.model.exception.AbsentCookieException;
import com.ivo.codebin.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CookieServiceImplementation implements CookieService {

    @Override
    public ResponseCookie generateCookie(String key, String value, Long maxAge, String path, String domain, String sameSite, boolean httpOnly, boolean secure) {
        return ResponseCookie.from(key, value)
                .maxAge(maxAge)
                .path(path) // if path is "/", it is a globally available path (http://localhost:8080/*)
                .domain(domain)
                .sameSite(sameSite)
                .httpOnly(httpOnly)
                .secure(secure)
                .build();
    }

    @Override
    public String extractCookie(HttpServletRequest request, String key) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(key))
                .map(Cookie::getValue)
                .findAny()
                .orElseThrow(() -> new AbsentCookieException(key + " cookie is absent!"));
    }

    @Override
    public boolean authCookieExists(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .anyMatch(cookie -> cookie.getName().equals("access-token") || cookie.getName().equals("refresh-token"));
        // returns true if either "access-token" or "refresh-token" cookie is present
    }

}
