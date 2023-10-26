package com.ivo.codebin.model.base;

import com.ivo.codebin.model.User;
import com.ivo.codebin.model.enumerations.TokenType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @ManyToOne
    private User user;

    public Token(String token, TokenType tokenType, User user) {
        this.token = token;
        this.tokenType = tokenType;
        this.user = user;
    }

}
