package com.ivo.codebin.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String extractUsername(String token);

    Date extractExpiration(String token);

}
