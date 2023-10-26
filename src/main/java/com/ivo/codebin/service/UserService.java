package com.ivo.codebin.service;

import com.ivo.codebin.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findById(String id);

    boolean exists(String username);

    User save(User user);

    User getUserData(String accessToken);

}
