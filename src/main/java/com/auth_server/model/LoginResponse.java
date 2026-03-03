package com.auth_server.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}
