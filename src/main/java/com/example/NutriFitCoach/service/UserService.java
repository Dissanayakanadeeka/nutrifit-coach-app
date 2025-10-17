package com.example.NutriFitCoach.service;

import com.example.NutriFitCoach.entity.User;
import com.example.NutriFitCoach.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateProfile(User user) {
        return userRepository.save(user);
    }
}
