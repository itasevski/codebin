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

    @JsonProperty("csrf_token")
    private String csrfToken;

    // we don't need to include the access or refresh tokens here, since they are set as cookies in the user's browser and won't be accessed or manipulated
    // through JavaScript.

}
