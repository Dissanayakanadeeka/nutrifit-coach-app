package com.example.NutriFitCoach.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "meal_logs", indexes = {
    @Index(name = "idx_meal_user_consumedAt", columnList = "user_id, consumedAt")
})

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;

    // quantity in grams (store canonical unit)
    @Column(nullable = false)
    private Double quantityGrams;

    // computed nutritional values (for quick aggregates)
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;

    private LocalDateTime consumedAt; // when user ate it
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() { createdAt = LocalDateTime.now(); }
    @PreUpdate
    public void onUpdate() { updatedAt = LocalDateTime.now(); }
}