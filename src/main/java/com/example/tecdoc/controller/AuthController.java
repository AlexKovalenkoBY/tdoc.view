package com.example.tecdoc.controller;

import com.example.tecdoc.JwtUtil;
import com.example.tecdoc.repository.UserRepository;
import com.example.tecdoc.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(Map.of("token", token));
    }
@GetMapping("/me")
public ResponseEntity<?> getMe(Authentication auth) {
    User user = userRepository.findByUsername(auth.getName()).orElse(null);
    if (user == null) {
        return ResponseEntity.status(404).body("User not found");
    }

    Map<String, Object> result = new HashMap<>();
    result.put("username", user.getUsername());
    result.put("fullName", user.getFullName());
    result.put("roles", user.getRoles());

    return ResponseEntity.ok(result);
}

}
