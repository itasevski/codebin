package com.ivo.codebin.service;

public interface CsrfTokenService {

    String generateToken(String accessToken);

    String generateTokenForUsername(String username);

    void removeToken(String username);

    boolean isTokenValid(String username, String receivedCsrfToken);

    void loadTokens();

    void persistTokens();

    void deleteTokens();

}
