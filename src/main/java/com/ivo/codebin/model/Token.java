package com.ivo.codebin.model;

import com.ivo.codebin.model.enumerations.TokenType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean revoked;

    private boolean expired;

    @ManyToOne
    private User user;

    public Token(String token, TokenType tokenType, boolean revoked, boolean expired, User user) {
        this.token = token;
        this.tokenType = tokenType;
        this.revoked = revoked;
        this.expired = expired;
        this.user = user;
    }

}
