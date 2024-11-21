package com.slobodianyk.java_spring_boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;

public class TodoTestHelper {

    private final ObjectMapper objectMapper;

    public TodoTestHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String createTodoRequest(String title, String description, LocalDateTime dueDate, String priority, String status) {
        TodoCreateDto todoCreateDto = new TodoCreateDto(title, description, dueDate, priority, status);

        try {
            return objectMapper.writeValueAsString(todoCreateDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize request body", e);
        }
    }

    public static class TodoCreateDto {
        private String title;
        private String description;
        private LocalDateTime dueDate;
        private String priority;
        private String status;

        public TodoCreateDto(String title, String description, LocalDateTime dueDate, String priority, String status) {
            this.title = title;
            this.description = description;
            this.dueDate = dueDate;
            this.priority = priority;
            this.status = status;
        }
    }
}
