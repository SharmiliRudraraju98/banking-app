package com.example.gatewayservice.controller;

import com.example.gatewayservice.model.User;
import com.example.gatewayservice.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Hardcoded user for now
    private final User demoUser = new User("admin", "admin123", "ROLE_ADMIN");

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        if (demoUser.getUsername().equals(username) &&
                demoUser.getPassword().equals(password)) {
            String token = jwtUtil.generateToken(username, demoUser.getRole());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return Mono.just(ResponseEntity.ok(response));
        }

        return Mono.just(ResponseEntity.status(401).build());
    }
}