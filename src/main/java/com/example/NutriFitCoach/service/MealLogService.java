package com.example.NutriFitCoach.service;

import com.example.NutriFitCoach.entity.*;
import com.example.NutriFitCoach.repository.*;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import com.example.NutriFitCoach.exception.ResourceNotFoundException;
import com.example.NutriFitCoach.dto.MealSummaryDTO;
@Service
@Transactional
public class MealLogService {
    private final MealLogRepository mealRepo;
    private final FoodItemRepository foodRepo;
    private final UserRepository userRepo; // your existing repo
    private final FoodService foodService;

    public MealLogService(MealLogRepository mealRepo, FoodItemRepository foodRepo,
                         UserRepository userRepo, FoodService foodService) {
        this.mealRepo = mealRepo; this.foodRepo = foodRepo; this.userRepo = userRepo; this.foodService = foodService;
    }

    public MealLog addMealLog(String username, Long foodItemId, double grams, LocalDateTime consumedAt) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", username));
        FoodItem food = foodRepo.findById(foodItemId).orElseThrow(() -> new ResourceNotFoundException("FoodItem", foodItemId));
        FoodService.Nutrition n = foodService.computeForQuantity(food, grams);

        MealLog m = MealLog.builder()
                .user(user)
                .foodItem(food)
                .quantityGrams(grams)
                .calories(n.calories)
                .protein(n.protein)
                .carbs(n.carbs)
                .fat(n.fat)
                .consumedAt(consumedAt == null ? LocalDateTime.now() : consumedAt)
                .build();
        System.out.println("🟡 Saving meal log for user ID: " + m.getUser().getId());
        return mealRepo.save(m);
    }

    public List<MealLog> getForDate(String username, LocalDate date) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", username));
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return mealRepo.findByUserAndConsumedAtBetweenOrderByConsumedAtAsc(user, start, end);
    }

public NutritionSummary getSummaryForDate(String username, LocalDate date) {
    User user = userRepo.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", username));

    LocalDateTime start = date.atStartOfDay();
    LocalDateTime end = date.atTime(LocalTime.MAX);

    MealSummaryDTO row = mealRepo.summaryTotals(user, start, end);

    // row will never be null because of coalesce(0)
    double calories = row.getCalories() != null ? row.getCalories() : 0.0;
    double protein  = row.getProtein() != null ? row.getProtein() : 0.0;
    double carbs    = row.getCarbs() != null ? row.getCarbs() : 0.0;
    double fat      = row.getFat() != null ? row.getFat() : 0.0;

    return new NutritionSummary(calories, protein, carbs, fat);
}



    private double toDouble(Object o) { return o == null ? 0.0 : ((Number)o).doubleValue(); }

    public static record NutritionSummary(double calories,double protein,double carbs,double fat) {}
}
