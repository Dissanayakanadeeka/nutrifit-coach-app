package com.example.NutriFitCoach.controller;

import com.example.NutriFitCoach.entity.User;
import com.example.NutriFitCoach.dto.LoginRequest;
import com.example.NutriFitCoach.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.NutriFitCoach.dto.SignupRequest;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(
            authService.registerUser(request.getUsername(), request.getEmail(), request.getPassword())
        );
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        String token = authService.loginUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(Map.of("token", token));
    }


@GetMapping("/users")
public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = authService.getAllUsers(); // <-- make sure this method exists
    return ResponseEntity.ok(users);
}



}
