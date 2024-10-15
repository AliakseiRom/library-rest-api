package com.example.library_rest_api.auth;

import lombok.Builder;

@Builder(setterPrefix = "set", toBuilder = true)
public record AuthRequest(
        String username,
        String password
) {}