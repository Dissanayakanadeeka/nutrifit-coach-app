package com.example.NutriFitCoach.repository;

import com.example.NutriFitCoach.entity.MealLog;
import com.example.NutriFitCoach.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.NutriFitCoach.dto.MealSummaryDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface MealLogRepository extends JpaRepository<MealLog, Long> {

    List<MealLog> findByUserAndConsumedAtBetweenOrderByConsumedAtAsc(User user, LocalDateTime start, LocalDateTime end);

    @Query("""
       select 
         coalesce(sum(m.calories),0) as calories, 
         coalesce(sum(m.protein),0) as protein, 
         coalesce(sum(m.carbs),0) as carbs, 
         coalesce(sum(m.fat),0) as fat
       from MealLog m
       where m.user = :user and m.consumedAt between :start and :end
    """)
    MealSummaryDTO summaryTotals(
            @Param("user") User user,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
