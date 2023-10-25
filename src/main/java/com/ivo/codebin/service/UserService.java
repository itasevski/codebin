package com.ivo.codebin.service;

import com.ivo.codebin.model.User;
import com.ivo.codebin.model.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findById(String id);

    UserResponse getUserData(String accessToken);

}
