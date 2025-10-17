package com.example.NutriFitCoach.dto;

public record MealLogCreateDTO(Long foodItemId, Double quantityGrams, String consumedAtIso) {}