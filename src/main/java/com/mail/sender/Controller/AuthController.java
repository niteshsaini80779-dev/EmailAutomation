package com.mail.sender.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mail.sender.Services.AuthService;
import com.mail.sender.dto.LoginRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request) {

        // Validate request
        if (request == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Request body cannot be empty.");
        }

        if (request.getUsername() == null
                || request.getUsername().isBlank()) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Username is required.");
        }

        if (request.getPassword() == null
                || request.getPassword().isBlank()) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Password is required.");
        }

        // Authenticate user
        boolean authenticated = authService.login(request);

        if (authenticated) {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Login successful.");

        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password.");
    }
}