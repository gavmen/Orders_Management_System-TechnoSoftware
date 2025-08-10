package com.empresa.logistica.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Base controller providing health check and system information endpoints.
 * This serves as a foundation for our REST API structure.
 */
@RestController
@RequestMapping("/health")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class HealthController {

    @GetMapping
    public String health() {
        return "Orders Management System is running successfully!";
    }

    @GetMapping("/info")
    public SystemInfo info() {
        return new SystemInfo(
            "Orders Management System",
            "1.0.0",
            "Customer orders with credit limit validation"
        );
    }

    public record SystemInfo(String name, String version, String description) {}
}
