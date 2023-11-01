package com.ivo.codebin.service;

import com.ivo.codebin.model.JwtToken;
import com.ivo.codebin.model.User;
import com.ivo.codebin.model.enumerations.TokenType;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface JwtTokenService {

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String extractUsername(String token);

    User getTokenOwner(String token);

    Date extractExpiration(String token);

    Optional<JwtToken> findByToken(String token);

    List<JwtToken> findValidUserTokens(String username);

    List<JwtToken> findValidUserTokens(String username, TokenType tokenType);

    void save(JwtToken token);

    void saveAll(List<JwtToken> tokens);

}
