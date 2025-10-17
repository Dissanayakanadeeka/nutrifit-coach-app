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
        return mealRepo.save(m);
    }

    public List<MealLog> getForDate(String username, LocalDate date) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", username));
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return mealRepo.findByUserAndConsumedAtBetweenOrderByConsumedAtAsc(user, start, end);
    }

    public NutritionSummary getSummaryForDate(String username, LocalDate date) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", username));
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        Object[] row = mealRepo.summaryTotals(user, start, end);
        double c = row == null ? 0.0 : toDouble(row[0]);
        double p = row == null ? 0.0 : toDouble(row[1]);
        double cr = row == null ? 0.0 : toDouble(row[2]);
        double f = row == null ? 0.0 : toDouble(row[3]);
        return new NutritionSummary(c, p, cr, f);
    }

    private double toDouble(Object o) { return o == null ? 0.0 : ((Number)o).doubleValue(); }

    public static record NutritionSummary(double calories,double protein,double carbs,double fat) {}
}
