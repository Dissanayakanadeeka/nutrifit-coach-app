package com.example.NutriFitCoach.controller;
import com.example.NutriFitCoach.entity.UserProfile;
import com.example.NutriFitCoach.entity.User;
import com.example.NutriFitCoach.repository.UserProfileRepository;
import com.example.NutriFitCoach.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin
public class UserProfileController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository profileRepository;

    @GetMapping("/{username}")
    public Optional<UserProfile> getProfile(@PathVariable String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return profileRepository.findByUser(user);
    }

    @PostMapping("/{username}")
    public UserProfile saveOrUpdateProfile(
            @PathVariable String username,
            @RequestBody UserProfile profileData) {

        User user = userRepository.findByUsername(username).orElseThrow();

        Optional<UserProfile> existing = profileRepository.findByUser(user);
        if (existing.isPresent()) {
            UserProfile profile = existing.get();
            System.out.println("💥Updating existing profile for user: " + profileData.getHeightCm());
            profile.setAge(profileData.getAge());
            profile.setGoal(profileData.getGoal());
            profile.setDietType(profileData.getDietType());
            profile.setDailyCalorieTarget(profileData.getDailyCalorieTarget());
            profile.setAllergies(profileData.getAllergies());
            profile.setActivityLevel(profileData.getActivityLevel());
            profile.setGender(profileData.getGender());
            profile.setHeightCm(profileData.getHeightCm());
            profile.setWeightKg(profileData.getWeightKg());
            return profileRepository.save(profile);
        } else {
            profileData.setUser(user);
            return profileRepository.save(profileData);
        }
    }
}
