package com.slobodianyk.java_spring_boot.mapper;

import com.slobodianyk.java_spring_boot.dto.TaskHistoryResponseDto;
import com.slobodianyk.java_spring_boot.models.TaskHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskHistoryMapper {

    @Mapping(target = "todoId", source = "todo.id")
    TaskHistoryResponseDto toResponseDto(TaskHistory taskHistory);
}
