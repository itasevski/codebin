package com.ivo.codebin.web.restcontroller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/test")
// @CrossOrigin(origins = "http://localhost:3000")
// we don't need this now since we have a CORS configuration bean that specifies the allowed origin/s.
// @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
// when we set allowCredentials to "true", we allow requests to contain cookies in them. If this wasn't present, cookies would have been omitted, resulting in a
// 400 Bad Request upon parsing the cookies with the @CookieValue annotation. View the FE side of this implementation and cookie integration with Axios.
// Since we don't need this annotation now, this functionality can be enabled in the CORS configuration in ApplicationConfiguration.java. (setAllowCredentials)
public class TestRestController {

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello from protected resource");
    }

//    @GetMapping
//    public ResponseEntity<String> hello(@CookieValue("access-token") String token) {
//        System.out.println(token);
//        return ResponseEntity.ok("hello from protected resource");
//    }

}
