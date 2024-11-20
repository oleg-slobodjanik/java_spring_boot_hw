package com.slobodianyk.java_spring_boot.dto;

import com.slobodianyk.java_spring_boot.dto.todoEnum.Priority;
import com.slobodianyk.java_spring_boot.dto.todoEnum.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record TodoCreateDto(
        // title: String (required, limited to 100 characters, e.g., max length = 100)
        // description: String (optional, but if present, limited to 500 characters)
        // dueDate: LocalDateTime (required)
        // priority: enum (optional, can be values like LOW, MEDIUM, HIGH)
        @NotBlank(message = "Title cannot be blank")
        @Length(max = 100, message = "Title must be 100 characters or fewer")
        String title,

        @Size(max = 500, message = "Description should not exceed 500 characters")
        String description,

        @NotNull(message = "Due date is required")
        LocalDateTime dueDate,

        Priority priority,

        @NotNull(message = "Status is required")
        Status status
) {
    public Priority priority() {
        return priority != null ? priority : Priority.MEDIUM;
    }
}
