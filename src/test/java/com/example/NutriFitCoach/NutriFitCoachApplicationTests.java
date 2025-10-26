package com.example.NutriFitCoach;

import com.example.NutriFitCoach.security.JwtAuthenticationFilter;
import com.example.NutriFitCoach.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

// ✅ This ensures that the Spring Boot application context loads
// without trying to initialize real security beans
@SpringBootTest
class NutriFitCoachApplicationTests {

    // Mock the security-related beans so that context can start cleanly
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void contextLoads() {
        // Just checks if the Spring context loads successfully
    }
}
