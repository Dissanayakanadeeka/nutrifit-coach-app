// src/main/java/com/example/NutriFitCoach/dto/SignupRequest.java
package com.example.NutriFitCoach.dto;

public class SignupRequest {
    private String username;
    private String password;
    private String email;

    // ✅ Required: getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
