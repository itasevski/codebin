package com.ivo.codebin.repository;

import com.ivo.codebin.model.Token;
import com.ivo.codebin.model.enumerations.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = "SELECT t FROM Token t WHERE t.user.username=:username AND (t.expired = false OR t.revoked = false)")
    List<Token> findValidUserTokens(String username);

    @Query(value = "SELECT t FROM Token t WHERE t.user.username=:username AND t.tokenType=:tokenType AND (t.expired = false OR t.revoked = false)")
    List<Token> findValidUserTokens(String username, TokenType tokenType);

    Optional<Token> findByToken(String token);

}
