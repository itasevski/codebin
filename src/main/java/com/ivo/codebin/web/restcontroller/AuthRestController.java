package com.ivo.codebin.web.restcontroller;

import com.ivo.codebin.model.User;
import com.ivo.codebin.model.dto.LoginDto;
import com.ivo.codebin.model.dto.RegistrationDto;
import com.ivo.codebin.model.response.AuthenticationResponse;
import com.ivo.codebin.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationDto registrationDto) {
        this.authService.register(registrationDto);
        return ResponseEntity.ok().body(String.format("%s successfully registered.", registrationDto.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        // the login method can throw an exception which in turn would result in an instant 403 Forbidden response without returning the 200 response from this
        // ResponseEntity, so it is OK to directly put the result of the method in the body of the ResponseEntity.
        return ResponseEntity.ok().body(this.authService.login(loginDto, response));
    }

    // Refresh token process:
    // 1. User sends a request with the access and refresh token cookies (body of request is empty, that's why we dont receive a RequestBody here)
    // 2. AuthFilter checks refresh token (based on the request URL, then from the extractUsername method), sees that token is valid, updates the SecurityContext
    // for the user (for the current request) and triggers execution of the other filters
    // 3. The request finally reaches this endpoint, service method is called
    // 4. Service method checks again whether refresh token cookie is present and valid, generates new access and refresh tokens for user and revokes previous access and refresh tokens in database
    // 5. Service method saves new access and refresh tokens in database, sets the new cookies in the user's browser and returns AuthenticationResponse containing only the
    // username and new access token, leaving the refresh token field empty for security purposes
    // Note: if refresh token is invalid (has expired), we must kick the user back to the login screen. At this point, the last access and refresh tokens remain valid in the database. But,
    // once the user logs in again, they will be revoked and new ones will be generated.
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.authService.refreshToken(request, response);
    }

    // Note: filters get executed before the actual request reaches our REST API
}
