package com.example.NutriFitCoach.controller;
import com.example.NutriFitCoach.entity.UserProfile;
import com.example.NutriFitCoach.entity.User;
import com.example.NutriFitCoach.repository.UserProfileRepository;
import com.example.NutriFitCoach.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

import java.util.Optional;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin
public class RecommendationController {

    @Autowired
    private UserProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{username}")
    public Map<String, Object> getRecommendations(@PathVariable String username){
        User user = userRepository.findByUsername(username).orElseThrow( () -> new RuntimeException("User not found"));
        UserProfile profile = profileRepository.findByUser(user).orElseThrow( () -> new RuntimeException("User profile not found"));

        Map<String, Object> recommendations = new HashMap<>();
        Double bmr = calculateBMR(profile);
        Double adjustedCalories = adjustCaloriesForGoal(bmr, profile.getGoal());
        Map<String, Double> macros = calculateMacros(adjustedCalories, profile.getGoal());
        recommendations.put("dailyCalories", adjustedCalories);
        recommendations.put("macros", macros);  
        recommendations.put("bmr", bmr);
        recommendations.put("goal", profile.getGoal());
        return recommendations;
        


    }

    private Double calculateBMR(UserProfile profile){
        Double bmr;
        if(profile.getGender().equalsIgnoreCase("male")){
            bmr = 10 * profile.getWeightKg() + 6.25 * profile.getHeightCm() - 5 * profile.getAge() + 5;
        } else {
            bmr = 10 * profile.getWeightKg() + 6.25 * profile.getHeightCm() - 5 * profile.getAge() - 161;
        }
        return bmr;
    }

    private Double adjustCaloriesForGoal(double bmr, String goal){
        switch (goal.toLowerCase()){
            case "lose weight": return bmr - 500;
            case "gain muscle": return bmr + 300;
            default: return bmr;
        }
    }

    private Map<String, Double> calculateMacros(double calories, String goal) {
        Map<String, Double> macros = new HashMap<>();
        macros.put("protein", calories * 0.3 / 4); // 30% protein
        macros.put("fat", calories * 0.25 / 9);   // 25% fat
        macros.put("carbs", calories * 0.45 / 4); // 45% carbs
        return macros;
    }


    
}