package com.ivo.codebin.repository;

import com.ivo.codebin.model.JwtToken;
import com.ivo.codebin.model.enumerations.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Integer> {

    @Query(value = "SELECT t FROM JwtToken t WHERE t.user.username=:username AND (t.expired = false OR t.revoked = false)")
    List<JwtToken> findValidUserTokens(String username);

    @Query(value = "SELECT t FROM JwtToken t WHERE t.user.username=:username AND t.tokenType=:tokenType AND (t.expired = false OR t.revoked = false)")
    List<JwtToken> findValidUserTokens(String username, TokenType tokenType);

    Optional<JwtToken> findByToken(String token);

}
