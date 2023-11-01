package com.ivo.codebin.service.implementation;

import com.ivo.codebin.configuration.security.constants.AuthConstants;
import com.ivo.codebin.model.JwtToken;
import com.ivo.codebin.model.User;
import com.ivo.codebin.model.base.Token;
import com.ivo.codebin.model.enumerations.TokenType;
import com.ivo.codebin.repository.JwtTokenRepository;
import com.ivo.codebin.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class JwtTokenServiceImplementation implements JwtTokenService {
    private final JwtTokenRepository jwtTokenRepository;

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    // A function for generating a JWT.
    // The extraClaims map object is sent along if we want to add any additional extra claims, besides the existing ones (subject, issued and expiration).
    @Override
    public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, AuthConstants.ACCESS_TOKEN_EXPIRATION_TIME);
    }

    @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, AuthConstants.REFRESH_TOKEN_EXPIRATION_TIME);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return (userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }

    // A function for extracting a single claim from a token.
    // Example: Claims::getSubject is a type of Function<Claims, String>, so when we call this method with the token and that function
    // as a parameter, after all the Claims have been extracted, we use the claimsResolver to resolve only the subject claim from
    // all the other claims from the JWT token.
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public User getTokenOwner(String token) {
        Optional<JwtToken> existingToken = findByToken(token);
        return existingToken.map(Token::getUser).orElse(null);
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public Optional<JwtToken> findByToken(String token) {
        return this.jwtTokenRepository.findByToken(token);
    }

    @Override
    public List<JwtToken> findValidUserTokens(String username) {
        return this.jwtTokenRepository.findValidUserTokens(username);
    }

    @Override
    public List<JwtToken> findValidUserTokens(String username, TokenType tokenType) {
        return this.jwtTokenRepository.findValidUserTokens(username, tokenType);
    }

    @Override
    public void save(JwtToken token) {
        this.jwtTokenRepository.save(token);
    }

    @Override
    public void saveAll(List<JwtToken> tokens) {
        this.jwtTokenRepository.saveAll(tokens);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // subject claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // pass the key and the desired hashing algorithm (note: we are using a 32-byte key, so therefore we use HMAC SHA 256 bit hashing algorithm)
                .compact(); // creates the token
    }

    // A JWT (JSON WEB TOKEN) is composed of three parts: header (metadata - type of token and encoding algorithm used), payload (the data/claims -
    // subject, name, email...) and signature.
    // Claims are fields of the JWT's payload. There are three types of claims: registered, private and public. The payload can contain fields (claims)
    // such as subject, name, email, IAT etc.
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token) // JWS - JSON Web Signature // This "parseClaimsJws" method also checks the token for validity and throws an ExpiredJwtException if token has expired
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // A function for generating a signing key of 32 bytes.
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(AuthConstants.SECRET.getBytes(StandardCharsets.UTF_8));
    }

}
