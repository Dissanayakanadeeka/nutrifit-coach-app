package com.example.NutriFitCoach.controller;

import com.example.NutriFitCoach.entity.FoodItem;
import com.example.NutriFitCoach.dto.FoodItemDTO;
import com.example.NutriFitCoach.dto.FoodItemCreateDTO;
import com.example.NutriFitCoach.repository.FoodItemRepository;
import com.example.NutriFitCoach.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;
    private final FoodItemRepository foodItemRepository;

    @GetMapping
    public Page<FoodItemDTO> search(@RequestParam(required = false) String q,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int size) {
        Pageable p = PageRequest.of(page, size);
        return foodService.search(q, p).map(f -> new FoodItemDTO(
            f.getId(), f.getName(), f.getBrand(), f.getCaloriesPer100g(),
            f.getProteinPer100g(), f.getCarbsPer100g(), f.getFatPer100g(),
            f.getServingSizeGrams(), f.getServingDescription(), f.getBarcode()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItemDTO> get(@PathVariable Long id) {
        return foodService.findById(id)
            .map(f -> ResponseEntity.ok(new FoodItemDTO(
                f.getId(), f.getName(), f.getBrand(), f.getCaloriesPer100g(),
                f.getProteinPer100g(), f.getCarbsPer100g(), f.getFatPer100g(),
                f.getServingSizeGrams(), f.getServingDescription(), f.getBarcode()
            )))
            .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<FoodItemDTO> create(@RequestBody FoodItemCreateDTO dto) {
        FoodItem f = FoodItem.builder()
            .name(dto.name())
            .brand(dto.brand())
            .caloriesPer100g(dto.caloriesPer100g())
            .proteinPer100g(dto.proteinPer100g())
            .carbsPer100g(dto.carbsPer100g())
            .fatPer100g(dto.fatPer100g())
            .servingSizeGrams(dto.servingSizeGrams())
            .servingDescription(dto.servingDescription())
            .barcode(dto.barcode())
            .build();

        FoodItem saved = foodService.create(f);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(saved));
    }

    @GetMapping("/{id}/nutrition")
    public ResponseEntity<?> getNutritionForQuantity(@PathVariable Long id,
                                                     @RequestParam Double quantityGrams) {
        var opt = foodService.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        var n = foodService.computeForQuantity(opt.get(), quantityGrams);
        return ResponseEntity.ok(Map.of(
            "calories", n.calories,
            "protein", n.protein,
            "carbs", n.carbs,
            "fat", n.fat
        ));
    }
    private FoodItemDTO mapToDto(FoodItem item) {
    return new FoodItemDTO(
        item.getId(),
        item.getName(),
        item.getBrand(),
        item.getCaloriesPer100g(),
        item.getProteinPer100g(),
        item.getCarbsPer100g(),
        item.getFatPer100g(),
        item.getServingSizeGrams(),
        item.getServingDescription(),
        item.getBarcode()
    );
}


    // Search foods by name (for typeahead)
    @GetMapping("/search")
    public List<FoodItem> searchFoodItems(@RequestParam String query) {
        return foodItemRepository
                .findByNameContainingIgnoreCase(query, PageRequest.of(0, 10)) // limit to 10 results
                .getContent();
    }

    @GetMapping("/all")
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

}
