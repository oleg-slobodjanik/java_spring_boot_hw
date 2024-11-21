package com.slobodianyk.java_spring_boot.dto;

import com.slobodianyk.java_spring_boot.dto.todoEnum.Priority;
import com.slobodianyk.java_spring_boot.dto.todoEnum.Status;

import java.time.LocalDateTime;

public record TodoResponseDto(
        Long id,
        String title,
        String description,
        LocalDateTime dueDate,
        Priority priority,
        Status status,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        Long userId
) {
}
