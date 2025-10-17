package com.example.NutriFitCoach.dto;

public record MealLogDTO(Long id, Long foodItemId, String foodName, Double quantityGrams,
                         Double calories, Double protein, Double carbs, Double fat,
                         String consumedAtIso) {}