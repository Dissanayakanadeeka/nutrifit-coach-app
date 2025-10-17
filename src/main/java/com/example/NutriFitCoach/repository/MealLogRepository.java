package com.example.NutriFitCoach.repository;

import com.example.NutriFitCoach.entity.MealLog;
import com.example.NutriFitCoach.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MealLogRepository extends JpaRepository<MealLog, Long>{
    List<MealLog> findByUserAndConsumedAtBetweenOrderByConsumedAtAsc(User user, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(m.calories),0), COALESCE(SUM(m.protein),0), COALESCE(SUM(m.carbs),0), COALESCE(SUM(m.fat),0) " +
           "FROM MealLog m WHERE m.user = :user AND m.consumedAt BETWEEN :start AND :end")
    Object[] summaryTotals(@Param("user") User user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}