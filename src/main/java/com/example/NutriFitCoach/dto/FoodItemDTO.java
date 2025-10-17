package com.example.NutriFitCoach.dto;

public record FoodItemDTO(Long id, String name, String brand, Double caloriesPer100g,
                          Double proteinPer100g, Double carbsPer100g, Double fatPer100g,
                          Double servingSizeGrams, String servingDescription, String barcode) {}
