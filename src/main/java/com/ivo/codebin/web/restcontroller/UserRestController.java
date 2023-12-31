package com.ivo.codebin.web.restcontroller;

import com.ivo.codebin.model.User;
import com.ivo.codebin.model.response.UserResponse;
import com.ivo.codebin.service.CsrfTokenService;
import com.ivo.codebin.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;
    private final CsrfTokenService csrfTokenService;

    @GetMapping
    public ResponseEntity<UserResponse> getUserData(@CookieValue("access-token") String accessToken) {
        User user = this.userService.getUserData(accessToken);
        return ResponseEntity.ok(
                UserResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                // for every request to fetch user data, we generate a new CSRF token for the current session. On the client-side, this endpoint gets called
                // for every authenticated user, upon initial page load and on every refresh, indicating a new session which requires a new CSRF token.
                .csrfToken(this.csrfTokenService.generateTokenForUsername(user.getUsername()))
                .build());
    }

}
