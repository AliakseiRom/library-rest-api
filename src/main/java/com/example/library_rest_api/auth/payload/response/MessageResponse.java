package com.example.library_rest_api.auth.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {

    String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}
