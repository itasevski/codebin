package com.ivo.codebin.model;

import com.ivo.codebin.model.base.Token;
import com.ivo.codebin.model.enumerations.TokenType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "csrf_token")
@EqualsAndHashCode(callSuper = true)
public class CsrfToken extends Token {
    public CsrfToken(String token, TokenType tokenType, User user) {
        super(token, tokenType, user);
    }
}
