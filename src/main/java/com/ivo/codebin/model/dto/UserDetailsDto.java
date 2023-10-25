package com.ivo.codebin.model.dto;

import com.ivo.codebin.model.User;
import com.ivo.codebin.model.enumerations.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetailsDto {

    private String username;
    private Role role;

    public static UserDetailsDto of(User user) {
        return new UserDetailsDto(user.getUsername(), user.getRole());
    }

}
