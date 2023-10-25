package com.ivo.codebin.web.restcontroller;

import com.ivo.codebin.model.response.UserResponse;
import com.ivo.codebin.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> getUserData(@CookieValue("access-token") String accessToken) {
        return ResponseEntity.ok(this.userService.getUserData(accessToken));
    }

}
