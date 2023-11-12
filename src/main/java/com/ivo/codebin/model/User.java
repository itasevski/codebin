package com.ivo.codebin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ivo.codebin.model.enumerations.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Table(name = "codebin_user")
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // the "mappedBy" property tells Hibernate that this relation is mapped by the "user" field in the JwtToken class, preventing the creation of an additional
    // table to satisfy this type of relationship. However, if we retrieve an entity from this class by sending a request to our REST controller, recursion
    // issues will occur, since the Jackson library doesn't care about this "mappedBy" property. That is why we use the @JsonIgnore annotation, which excludes
    // fields from classes that are serialized into JSON values.
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<JwtToken> jwtTokens;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<CsrfToken> csrfTokens;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Snippet> snippets;

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(this.role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
