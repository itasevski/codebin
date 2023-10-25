package com.ivo.codebin.service.implementation;

import com.ivo.codebin.model.User;
import com.ivo.codebin.model.exception.UserNotFoundException;
import com.ivo.codebin.model.response.UserResponse;
import com.ivo.codebin.repository.UserRepository;
import com.ivo.codebin.service.CsrfService;
import com.ivo.codebin.service.JwtService;
import com.ivo.codebin.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CsrfService csrfService;

    @Override
    public User findById(String id) {
        return findByUsername(id);
    }

    @Override
    public UserResponse getUserData(String accessToken) {
        String username = this.jwtService.extractUsername(accessToken);

        User user = findById(username);

        return UserResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .csrfToken(this.csrfService.generateCsrfTokenForUsername(username))
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " does not exist!"));
    }

    private User findByUsername(String username) {
        return this.userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("User with id " + username + " does not exist!"));
    }
}
