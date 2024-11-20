package com.slobodianyk.java_spring_boot.integration;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {

    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        // Configure H2 as the datasource
        registry.add("spring.datasource.url", () -> "jdbc:mysql://localhost:3306/java_db?useSSL=false");
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.datasource.username", () -> "root");
        registry.add("spring.datasource.password", () -> "123");
    }
}