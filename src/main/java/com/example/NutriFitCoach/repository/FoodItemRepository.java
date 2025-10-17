package com.example.NutriFitCoach.repository;

import com.example.NutriFitCoach.entity.FoodItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    Page<FoodItem> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
