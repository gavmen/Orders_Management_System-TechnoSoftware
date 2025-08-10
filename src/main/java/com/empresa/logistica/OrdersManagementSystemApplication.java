package com.empresa.logistica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Customer Orders Management System.
 * 
 * This system manages customer orders for a logistics company, implementing
 * credit limit validation based on the last 30 days of order history.
 * 
 * @author Gabriel Mendonca
 * @version 1.0.0
 */
@SpringBootApplication
public class OrdersManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersManagementSystemApplication.class, args);
    }
}
