package com.example.NutriFitCoach.controller;

import com.example.NutriFitCoach.entity.User;
import com.example.NutriFitCoach.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get logged-in user profile
    @GetMapping("/me")
    public ResponseEntity<User> getProfile(Principal principal) {
        return userService.getUserByUsername(principal.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update profile
    @PutMapping("/me")
    public ResponseEntity<User> updateProfile(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateProfile(user));
    }
}
