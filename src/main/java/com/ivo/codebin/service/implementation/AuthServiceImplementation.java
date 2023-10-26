package com.ivo.codebin.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.codebin.configuration.security.constants.AuthConstants;
import com.ivo.codebin.model.JwtToken;
import com.ivo.codebin.model.User;
import com.ivo.codebin.model.dto.LoginDto;
import com.ivo.codebin.model.dto.RegistrationDto;
import com.ivo.codebin.model.enumerations.Role;
import com.ivo.codebin.model.enumerations.TokenType;
import com.ivo.codebin.model.exception.PasswordMismatchException;
import com.ivo.codebin.model.exception.UserExistsException;
import com.ivo.codebin.model.response.AuthenticationResponse;
import com.ivo.codebin.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImplementation implements AuthService {

    private final UserService userService;
    private final CookieService cookieService;
    private final JwtTokenService jwtService;
    private final CsrfTokenService csrfService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public Optional<User> register(RegistrationDto registrationDto) {
        if(this.userService.exists(registrationDto.getUsername()))
            throw new UserExistsException("User with username " + registrationDto.getUsername() + " already exists!");

        if(!registrationDto.getPassword().equals(registrationDto.getConfirmPassword()))
            throw new PasswordMismatchException("Passwords do not match!");

        User user = new User(registrationDto.getUsername(), this.passwordEncoder.encode(registrationDto.getPassword()), Role.ROLE_USER);

        return Optional.of(this.userService.save(user));
    }

    @Override
    public AuthenticationResponse login(LoginDto loginDto, HttpServletResponse response) {
        // if authentication fails, an AuthenticationException is thrown and a 403 Forbidden error is returned.
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        User user = this.userService.findById(loginDto.getUsername());
        String accessToken = this.jwtService.generateAccessToken(user);
        String refreshToken = this.jwtService.generateRefreshToken(user);

        List<JwtToken> validUserTokens = this.jwtService.findValidUserTokens(user.getUsername());
        validUserTokens.forEach(validToken -> {
            validToken.setExpired(true);
            validToken.setRevoked(true);
        });
        this.jwtService.saveAll(validUserTokens);

        this.jwtService.save(new JwtToken(accessToken, TokenType.ACCESS, user, false, false));
        this.jwtService.save(new JwtToken(refreshToken, TokenType.REFRESH, user, false, false));

        ResponseCookie accessTokenCookie =
                this.cookieService.generateCookie(
                        "access-token", accessToken,
                        AuthConstants.HTTP_ACCESS_COOKIE_EXPIRATION_TIME,
                        "/", "localhost", "Strict",
                        true, false);
        ResponseCookie refreshTokenCookie =
                this.cookieService.generateCookie(
                        "refresh-token", refreshToken,
                        AuthConstants.HTTP_REFRESH_COOKIE_EXPIRATION_TIME,
                        "/", "localhost", "Strict",
                        true, false);
        // Creating HttpOnly cookies that can't be read with JavaScript and setting the sameSite policy to "Strict", thus protecting the user from XSS and CSRF attacks.
        // Since we have the same BE and FE domain (localhost), we can use the "Strict" sameSite policy.

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        // read the cookie-important-concepts.txt file for a detailed explanation of cookies and their properties.

        String csrfToken = this.csrfService.generateToken(accessToken);

        return AuthenticationResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .csrfToken(csrfToken)
                .build();

        // Note: no matter what type of exception we throw in this function with any response status code, if the exception is thrown, a 403 Forbidden response
        // is returned to the client. It might be possible that Spring Security catches these exceptions and returns custom 403 Forbidden responses, but it is
        // just a guess.

        // Update: I just tested this in a test rest controller by invoking the findById method from the UserService on a particular username that doesn't exist
        // and even though the UserNotFoundException should be thrown with a 404 Not Found status code, the server still returns 403 Forbidden responses to the
        // client, making the explanation above even more possible.

        // Update: After the implementation of the ControllerAdvice, I can confirm that indeed the case was as explained above. Any exception that is thrown during
        // runtime is caught by Spring Security and a custom 403 Forbidden response is returned to the client. After implementing the ControllerAdvice,
        // the exceptions now get caught by the controller advice, which returns wanted status codes and responses to the client based on the thrown exception,
        // thus preventing Spring Security from returning its own custom responses.
        // This was tested again by invoking the findById method from the UserService on a particular username that doesn't exist, and now a response with a 404 Not
        // Found status code was returned to the user.
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String username;
        final String refreshToken;

        refreshToken = this.cookieService.extractCookie(request, "refresh-token");
        username = this.jwtService.extractUsername(refreshToken);
        // no need to check presence of cookies, since our AuthFilter has already checked everything until this point.

        if (username != null) {
            User user = this.userService.findById(username);
            boolean isTokenNonExpiredAndNonRevoked = this.jwtService
                    .findByToken(refreshToken)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if (this.jwtService.isTokenValid(refreshToken, user) && isTokenNonExpiredAndNonRevoked) {
                String accessToken = this.jwtService.generateAccessToken(user);
                List<JwtToken> validUserAccessTokens = this.jwtService.findValidUserTokens(user.getUsername(), TokenType.ACCESS);
                validUserAccessTokens.forEach(validToken -> {
                    validToken.setExpired(true);
                    validToken.setRevoked(true);
                });
                this.jwtService.saveAll(validUserAccessTokens);
                this.jwtService.save(new JwtToken(accessToken, TokenType.ACCESS, user, false, false));

                ResponseCookie accessCookie =
                        this.cookieService.generateCookie(
                                "access-token", accessToken,
                                AuthConstants.HTTP_ACCESS_COOKIE_EXPIRATION_TIME,
                                "/", "localhost", "Lax",
                                true, false);

                response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .username(user.getUsername())
                        .accessToken(accessToken)
                        .refreshToken("")
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }


}
