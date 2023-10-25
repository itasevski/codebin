package com.ivo.codebin.web.restcontroller;

import com.ivo.codebin.service.CsrfService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/csrf")
public class CsrfRestController {

    private final CsrfService csrfService;

    @GetMapping
    public ResponseEntity<String> csrf(@CookieValue("access-token") String accessToken) {
        return ResponseEntity.ok(this.csrfService.generateCsrfToken(accessToken));
    }

    @GetMapping("/get")
    public String getCsrfToken(@CookieValue("access-token") String accessToken) {
        return this.csrfService.getCsrfToken(accessToken);
    }

    @PostMapping
    public ResponseEntity<String> testpost(HttpServletRequest request) {
        return ResponseEntity.ok("200 OK");
    }

}
