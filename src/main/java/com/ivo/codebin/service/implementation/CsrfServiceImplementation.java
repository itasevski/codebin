package com.ivo.codebin.service.implementation;

import com.ivo.codebin.repository.UserRepository;
import com.ivo.codebin.service.CsrfService;
import com.ivo.codebin.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor // only creates constructor for non-initialized values (CBDI for non-initialized values, in this case the JwtService)
public class CsrfServiceImplementation implements CsrfService {
    private final Map<String, String> storedCsrfTokens = new ConcurrentHashMap<>();
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public String generateCsrfToken(String accessToken) {
        String username = this.jwtService.extractUsername(accessToken);

        return generateToken(username);
    }

    @Override
    public String generateCsrfTokenForUsername(String username) {
        if(!this.userRepository.existsByUsername(username))
            throw new UsernameNotFoundException("User with username " + username + " does not exist!");

        return generateToken(username);
    }

    @Override
    public String getCsrfToken(String accessToken) {
        String username = this.jwtService.extractUsername(accessToken);

        return this.storedCsrfTokens.getOrDefault(username, "");
    }

    @Override
    public String printCsrfTokens() {
        StringBuilder stringBuilder = new StringBuilder();

        this.storedCsrfTokens.keySet()
                .forEach(key -> stringBuilder.append(String.format("%s: %s\n", key, this.storedCsrfTokens.get(key))));

        return stringBuilder.toString();
    }

    @Override
    public void removeCsrfToken(String username) {
        this.storedCsrfTokens.remove(username);
    }

    @Override
    public boolean isCsrfTokenValid(String username, String receivedCsrfToken) {
        if(!this.storedCsrfTokens.containsKey(username)) return false;

        System.out.println(this.storedCsrfTokens.get(username));
        System.out.println(receivedCsrfToken);

        return this.storedCsrfTokens.get(username).equals(receivedCsrfToken);
    }

    private String generateToken(String username) {
        String csrfToken = UUID.randomUUID().toString();
        this.storedCsrfTokens.put(username, csrfToken);
        return csrfToken;
    }

}
