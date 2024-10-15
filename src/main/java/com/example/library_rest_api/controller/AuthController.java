package com.example.library_rest_api.controller;

import com.example.library_rest_api.auth.AuthRequest;
import com.example.library_rest_api.auth.JwtTokenUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication", description = "API for user authentication and JWT generation")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate user", description = "Authenticate a user by username and password, and return a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated and token generated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data",
                    content = @Content)
    })
    public ResponseEntity<AuthDto> authenticate(@RequestBody
                                                @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                                                        @ExampleObject(
                                                                name = "ContractRemark",
                                                                value = """
                                                    {
                                                      "username": "admin",
                                                      "password": "admin"
                                                    }"""
                                                        )
                                                }))
                                                AuthRequest authRequest) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
        );

        User user = (User) authentication.getPrincipal();
        AuthDto authDto = new AuthDto(jwtTokenUtil.generateToken(user));
        return new ResponseEntity<>(authDto, HttpStatus.OK);
    }

    @Builder(setterPrefix = "set", toBuilder = true)
    private record AuthDto(@JsonProperty("access_token") String accessToken) {
    }
}
