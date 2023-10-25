package com.ivo.codebin.service;

public interface CsrfService {

    String generateCsrfToken(String accessToken);

    String generateCsrfTokenForUsername(String username);

    String getCsrfToken(String accessToken);

    String printCsrfTokens();

    void removeCsrfToken(String username);

    boolean isCsrfTokenValid(String username, String receivedCsrfToken);

}
