package com.example.NutriFitCoach.repository;
import com.example.NutriFitCoach.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.NutriFitCoach.entity.User;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(User user);
}