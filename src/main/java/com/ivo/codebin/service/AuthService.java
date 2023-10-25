package com.ivo.codebin.service;

import com.ivo.codebin.model.User;
import com.ivo.codebin.model.dto.LoginDto;
import com.ivo.codebin.model.dto.RegistrationDto;
import com.ivo.codebin.model.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public interface AuthService {

    Optional<User> register(RegistrationDto registrationDto);

    AuthenticationResponse login(LoginDto loginDto, HttpServletResponse response);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
