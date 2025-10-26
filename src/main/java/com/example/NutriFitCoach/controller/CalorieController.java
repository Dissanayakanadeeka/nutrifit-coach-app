package com.example.NutriFitCoach.controller;

import com.example.NutriFitCoach.dto.DailyCalorieDTO;
import com.example.NutriFitCoach.entity.MealLog;
import com.example.NutriFitCoach.entity.User;
import com.example.NutriFitCoach.repository.MealLogRepository;
import com.example.NutriFitCoach.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/calories")
@RequiredArgsConstructor
public class CalorieController {

    private final UserRepository userRepository;
    private final MealLogRepository mealLogRepository;

    @GetMapping("/last30days/{username}")
    public List<DailyCalorieDTO> getLast30DaysCalories(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(29); // last 30 days

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = today.atTime(23, 59, 59);

        // Fetch meal logs for the last 30 days
        List<MealLog> logs = mealLogRepository.findByUserAndConsumedAtBetweenOrderByConsumedAtAsc(
                user, startDateTime, endDateTime
        );

        // Group calories by day
        Map<LocalDate, Double> dailyCalories = new HashMap<>();
        for (MealLog log : logs) {
            LocalDate date = log.getConsumedAt().toLocalDate();
            dailyCalories.put(date, dailyCalories.getOrDefault(date, 0.0) + (log.getCalories() != null ? log.getCalories() : 0.0));
        }

        // Prepare DTO list, ensure all 30 days are included
        List<DailyCalorieDTO> result = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            LocalDate date = startDate.plusDays(i);
            result.add(new DailyCalorieDTO(
                    date.format(DateTimeFormatter.ISO_DATE),
                    dailyCalories.getOrDefault(date, 0.0)
            ));
        }

        return result;
    }
}
