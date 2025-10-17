package com.example.NutriFitCoach.entity;
import jakarta.persistence.Entity;
import lombok.*;
import java.time.LocalDateTime;
import jakarta.persistence.*;   

@Entity
@Table(name = "food_items")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String brand;

    @Column(nullable = false)
    private Double caloriesPer100g = 0.0;
    private Double proteinPer100g = 0.0;
    private Double carbsPer100g = 0.0;
    private Double fatPer100g = 0.0;

    private Double servingSizeGrams;
    private String servingDescription;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String barcode; 


    @PrePersist
    public void prePersist() { createdAt = LocalDateTime.now(); }
    @PreUpdate
    public void preUpdate() { updatedAt = LocalDateTime.now(); }
}