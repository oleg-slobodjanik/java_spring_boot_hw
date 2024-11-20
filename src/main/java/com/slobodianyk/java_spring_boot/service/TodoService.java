package com.slobodianyk.java_spring_boot.service;

import com.slobodianyk.java_spring_boot.dto.TaskHistoryResponseDto;
import com.slobodianyk.java_spring_boot.dto.TodoCreateDto;
import com.slobodianyk.java_spring_boot.dto.TodoResponseDto;
import com.slobodianyk.java_spring_boot.dto.TodoUpdateDto;

import java.util.List;

public interface TodoService {

    boolean existsById(Long id);

    void deleteById(Long id);

    List<TodoResponseDto> findAll();

    TodoResponseDto findById(Long id);

    List<TaskHistoryResponseDto> findTaskHistory(Long id);

    TodoResponseDto save(TodoCreateDto todoCreateDto);

    TodoResponseDto update(Long id, TodoUpdateDto todoUpdateDto);

    void delete(Long id);
}
