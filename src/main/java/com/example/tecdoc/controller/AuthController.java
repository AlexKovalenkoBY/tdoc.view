package com.example.tecdoc.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@RestController
@RequestMapping("/api")
public class AuthController {
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        // Ваша логика аутентификации
        // Возвращаем JWT токен или другую информацию о сессии
        return ResponseEntity.ok(Map.of("token", "generated-jwt-token"));
    }
    
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}