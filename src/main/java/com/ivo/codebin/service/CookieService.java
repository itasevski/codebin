package com.ivo.codebin.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public interface CookieService {

    ResponseCookie generateCookie(String key, String value, Long maxAge, String path, String domain, String sameSite, boolean httpOnly, boolean secure);

    String extractCookie(HttpServletRequest request, String key);

    boolean authCookieExists(HttpServletRequest request);

}
