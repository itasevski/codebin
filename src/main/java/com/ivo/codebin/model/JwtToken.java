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
@Table(name = "jwt_token")
@EqualsAndHashCode(callSuper = true)
public class JwtToken extends Token {
    private boolean revoked;
    private boolean expired;

    public JwtToken(String token, TokenType tokenType, User user, boolean revoked, boolean expired) {
        super(token, tokenType, user);
        this.revoked = revoked;
        this.expired = expired;
    }
}
