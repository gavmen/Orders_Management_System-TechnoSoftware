package com.empresa.logistica;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * Integration test to verify the Spring Boot application context loads correctly.
 * 
 * This test ensures that:
 * - All Spring components can be instantiated
 * - Database configuration is valid
 * - Application properties are correctly loaded
 * - No circular dependencies exist
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class OrdersManagementSystemApplicationTests {

    /**
     * Smoke test - verifies Spring context loads without errors.
     * This is the first test that should pass to confirm our setup is correct.
     */
    @Test
    void contextLoads() {
        // If this test passes, Spring Boot application context loaded successfully
        // This validates our Maven dependencies, configuration, and basic setup
    }
}
