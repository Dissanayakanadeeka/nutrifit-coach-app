package com.example.NutriFitCoach.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyCalorieDTO {
    private String date;  // formatted as yyyy-MM-dd
    private Double calories;
}
