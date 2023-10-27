package com.ivo.codebin.service.implementation;

import com.ivo.codebin.model.CsrfToken;
import com.ivo.codebin.model.enumerations.TokenType;
import com.ivo.codebin.repository.CsrfTokenRepository;
import com.ivo.codebin.service.CsrfTokenService;
import com.ivo.codebin.service.JwtTokenService;
import com.ivo.codebin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
// only creates constructor for non-initialized values (CBDI for non-initialized values, in this case the JwtService)
public class CsrfTokenServiceImplementation implements CsrfTokenService {

    // The ConcurrentHashMap is a high-performance hash map allowing fast, optimized and concurrent retrieval and update operations.
    // It is optimal for its purpose here, i.e. storing CSRF tokens for users and updating them for every new user session.
    private final Map<String, String> storedCsrfTokens = new ConcurrentHashMap<>();

    private final CsrfTokenRepository csrfTokenRepository;
    private final JwtTokenService jwtService;
    private final UserService userService;

    @Override
    public String generateToken(String accessToken) {
        String username = this.jwtService.extractUsername(accessToken);

        return generateCsrfToken(username);
    }

    @Override
    public String generateTokenForUsername(String username) {
        if (!this.userService.exists(username))
            throw new UsernameNotFoundException("User with username " + username + " does not exist!");

        return generateCsrfToken(username);
    }

    @Override
    public void removeToken(String username) {
        this.storedCsrfTokens.remove(username);
    }

    @Override
    public boolean isTokenValid(String username, String receivedCsrfToken) {
        if (!this.storedCsrfTokens.containsKey(username)) return false;

        return this.storedCsrfTokens.get(username).equals(receivedCsrfToken);
    }

    /**
     * A function that loads the user CSRF tokens from database to in-memory.
     * Called when server starts.
     */
    @Override
    public void loadTokens() {
        List<CsrfToken> csrfTokens = this.csrfTokenRepository.findAll();
        csrfTokens.forEach(token -> this.storedCsrfTokens.put(token.getUser().getUsername(), token.getToken()));
    }

    /**
     * A function that persists the user CSRF tokens in database from in-memory.
     * Called when server is shut down.
     */
    @Override
    public void persistTokens() {
        this.storedCsrfTokens.keySet()
                .forEach(key -> this.csrfTokenRepository.save(
                        new CsrfToken(
                                this.storedCsrfTokens.get(key),
                                TokenType.CSRF,
                                this.userService.findById(key)))
                );
    }

    @Override
    public void deleteTokens() {
        this.csrfTokenRepository.deleteAll();
    }

    private String generateCsrfToken(String username) {
        String csrfToken = UUID.randomUUID().toString();
        this.storedCsrfTokens.put(username, csrfToken);
        return csrfToken;
    }

}
