package com.ivo.codebin.model.dto;

import lombok.Data;

@Data
public class RegistrationDto {

    private String username;
    private String password;
    private String confirmPassword;

}
