package com.slobodianyk.java_spring_boot.mapper;

import com.slobodianyk.java_spring_boot.dto.TodoCreateDto;
import com.slobodianyk.java_spring_boot.dto.TodoResponseDto;
import com.slobodianyk.java_spring_boot.dto.TodoUpdateDto;
import com.slobodianyk.java_spring_boot.model.Todo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    Todo toEntity(TodoCreateDto todoCreateDto);

    Todo toEntity(TodoUpdateDto todoUpdateDto);

    TodoResponseDto toResponseDto(Todo todo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TodoUpdateDto dto, @MappingTarget Todo entity);
}
