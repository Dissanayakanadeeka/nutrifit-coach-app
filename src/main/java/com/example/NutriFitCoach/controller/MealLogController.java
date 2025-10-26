package com.example.NutriFitCoach.controller;
import com.example.NutriFitCoach.entity.MealLog;
import com.example.NutriFitCoach.service.MealLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.example.NutriFitCoach.dto.FoodItemDTO;
import com.example.NutriFitCoach.dto.FoodItemCreateDTO;
import com.example.NutriFitCoach.dto.MealLogDTO;
import com.example.NutriFitCoach.dto.MealLogCreateDTO;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
@CrossOrigin
public class MealLogController {
    private final MealLogService mealLogService;

    // Create log (user)
    @PostMapping
    public ResponseEntity<MealLogDTO> addMeal(@RequestBody MealLogCreateDTO dto, Authentication authentication) {
        String username = authentication.getName();
        LocalDateTime consumedAt = dto.consumedAtIso()==null ? null : LocalDateTime.parse(dto.consumedAtIso());
        MealLog m = mealLogService.addMealLog(username, dto.foodItemId(), dto.quantityGrams(), consumedAt);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(m));
    }

    // Get daily logs
    @GetMapping
    public ResponseEntity<?> getForDate(@RequestParam(required = false) String date, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        LocalDate d = date == null ? LocalDate.now() : LocalDate.parse(date);
        var result = mealLogService.getForDate(auth.getName(), d)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
}

    // Get summary
    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(@RequestParam(required = false) String date, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        LocalDate d = date == null ? LocalDate.now() : LocalDate.parse(date);
        return ResponseEntity.ok(mealLogService.getSummaryForDate(auth.getName(), d));
    }

    // helper mapping methods...
    private MealLogDTO mapToDto(MealLog log) {
    return new MealLogDTO(
        log.getId(),
        log.getFoodItem().getId(),
        log.getFoodItem().getName(),
        log.getQuantityGrams(),
        log.getCalories(),
        log.getProtein(),
        log.getCarbs(),
        log.getFat(),
        log.getConsumedAt().toString()
    );
}

}
