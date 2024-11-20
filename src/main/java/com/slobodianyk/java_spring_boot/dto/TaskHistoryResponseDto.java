package com.slobodianyk.java_spring_boot.dto;

import java.time.LocalDateTime;

public record TaskHistoryResponseDto(
        // id: Long (unique identifier of the task history entry)
        // todoId: Long (ID of the related TODO item)
        // oldState: String (state before the change)
        // newState: String (state after the change)
        // changeDate: LocalDateTime (timestamp when the change was made)
        // changedBy: String (the user who made the change)
        Long id,
        Long todoId,
        String oldState,
        String newState,
        LocalDateTime changeDate,
        String changedBy
) {
}
