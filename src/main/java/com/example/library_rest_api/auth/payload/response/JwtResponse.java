package com.example.library_rest_api.auth.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {

    private String jwt;

    private Integer id;

    private String username;

    private List<String> roles;

    public JwtResponse(String jwt, Integer id, String username, List<String> roles) {
        this.jwt = jwt;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
