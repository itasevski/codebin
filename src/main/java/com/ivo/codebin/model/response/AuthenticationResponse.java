package com.ivo.codebin.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ivo.codebin.model.enumerations.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder // @Builder (from Lombok) allows us to create an object with parameters without the use of constructors
public class AuthenticationResponse {

    private String username;

    private Role role;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("csrf_token")
    private String csrfToken;

}
