package com.ivo.codebin.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ivo.codebin.model.enumerations.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private String username;

    private Role role;

    @JsonProperty("csrf_token")
    private String csrfToken;

}
