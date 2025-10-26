package com.example.NutriFitCoach.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int age;
    private Double heightCm;
    private Double weightKg;
    private String gender;
    private String goal;
    private String dailyCalorieTarget;
    
    private String dietType;

    private String activityLevel;
     private String allergies;
}