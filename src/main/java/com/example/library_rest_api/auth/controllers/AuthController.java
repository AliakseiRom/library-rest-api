package com.example.library_rest_api.auth.controllers;

import com.example.library_rest_api.auth.payload.request.LoginRequest;
import com.example.library_rest_api.auth.payload.request.SignupRequest;
import com.example.library_rest_api.auth.payload.response.JwtResponse;
import com.example.library_rest_api.auth.payload.response.MessageResponse;
import com.example.library_rest_api.auth.security.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class AuthController {

    @Autowired
    private AuthService authService;

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
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody
                                                            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                                                                    @ExampleObject(
                                                                            name = "ContractRemark",
                                                                            value = """
                                                    {
                                                      "username": "admin3",
                                                      "password": "admin"
                                                    }"""
                                                                    )
                                                            }))
                                                            LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticate(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Register a new user with username, password, and other details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict - User already exists",
                    content = @Content)
    })
    public ResponseEntity<MessageResponse> registerUser(@RequestBody
                                                            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                                                                    @ExampleObject(
                                                                            name = "RegisterExample",
                                                                            value = """
                                                                                    {
                                                                                      "username": "newuser",
                                                                                      "password": "password123",
                                                                                      "roles": ["ROLE_USER", "ROLE_ADMIN"]
                                                                                    }"""
                                                                    )
                                                            }))SignupRequest signupRequest) {
        MessageResponse messageResponse = authService.register(signupRequest);
        return ResponseEntity.ok(messageResponse);
    }
}
