package com.example.NutriFitCoach.service;

import com.example.NutriFitCoach.entity.FoodItem;
import com.example.NutriFitCoach.repository.FoodItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.Optional;
import com.example.NutriFitCoach.dto.Nutrition;
import com.example.NutriFitCoach.exception.ResourceNotFoundException;

@Service
@Transactional
public class FoodService {
    private final FoodItemRepository foodRepo;
    public FoodService(FoodItemRepository foodRepo) { this.foodRepo = foodRepo; }

    public Page<FoodItem> search(String q, Pageable p) {
        if (q == null || q.isBlank()) return foodRepo.findAll(p);
        return foodRepo.findByNameContainingIgnoreCase(q, p);
    }

    public FoodItem create(FoodItem item) { return foodRepo.save(item); }
    public FoodItem update(Long id, FoodItem update) {
        FoodItem f = foodRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("FoodItem", id));
        // copy fields...
        f.setName(update.getName()); f.setBrand(update.getBrand());
        f.setCaloriesPer100g(update.getCaloriesPer100g());
        f.setProteinPer100g(update.getProteinPer100g());
        f.setCarbsPer100g(update.getCarbsPer100g());
        f.setFatPer100g(update.getFatPer100g());
        f.setServingSizeGrams(update.getServingSizeGrams());
        f.setServingDescription(update.getServingDescription());
        f.setBarcode(update.getBarcode());
        return foodRepo.save(f);
    }

    public Optional<FoodItem> findById(Long id) { return foodRepo.findById(id); }

    public Nutrition computeForQuantity(FoodItem food, double grams) {
        double factor = grams / 100.0;
        return new Nutrition(
            round(food.getCaloriesPer100g() * factor),
            round(food.getProteinPer100g() * factor),
            round(food.getCarbsPer100g() * factor),
            round(food.getFatPer100g() * factor)
        );
    }

    private double round(Double v) { return v == null ? 0.0 : Math.round(v * 100.0) / 100.0; }

    public static class Nutrition { public final double calories, protein, carbs, fat;
        public Nutrition(double c, double p, double cr, double f) { calories=c; protein=p; carbs=cr; fat=f; }
    }
}