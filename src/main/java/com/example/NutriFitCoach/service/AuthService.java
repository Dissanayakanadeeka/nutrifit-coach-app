package com.example.NutriFitCoach.service;

import com.example.NutriFitCoach.entity.User;
import com.example.NutriFitCoach.repository.UserRepository;
import com.example.NutriFitCoach.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;



@Service
public class AuthService {
    @Autowired
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User registerUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));


        return userRepository.save(user);
    }

    public String loginUser(String username, String password) {
        System.out.println("🔵 Authenticating user: " + username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("🟢 Authentication successful for user: " + username);
            return jwtUtil.generateToken(username);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
    public List<User> getAllUsers() {
    return userRepository.findAll();
}

}
