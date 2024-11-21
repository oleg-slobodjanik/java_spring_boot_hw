package com.slobodianyk.java_spring_boot.service.impl;

import com.slobodianyk.java_spring_boot.mapper.TodoMapper;
import com.slobodianyk.java_spring_boot.mapper.TaskHistoryMapper;

import com.slobodianyk.java_spring_boot.exception.ResourceNotFoundException;
import com.slobodianyk.java_spring_boot.dto.todoEnum.Status;
import com.slobodianyk.java_spring_boot.dto.TaskHistoryResponseDto;
import com.slobodianyk.java_spring_boot.dto.TodoCreateDto;
import com.slobodianyk.java_spring_boot.dto.TodoResponseDto;
import com.slobodianyk.java_spring_boot.dto.TodoUpdateDto;
import com.slobodianyk.java_spring_boot.models.TaskHistory;
import com.slobodianyk.java_spring_boot.models.Todo;
import com.slobodianyk.java_spring_boot.repository.TaskHistoryRepository;
import com.slobodianyk.java_spring_boot.repository.TodoRepository;

import com.slobodianyk.java_spring_boot.service.TodoService;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final TaskHistoryRepository taskHistoryRepository;
    private final TodoMapper todoMapper;
    private final TaskHistoryMapper taskHistoryMapper;

    @Override
    public List<TodoResponseDto> findAll() {
        List<Todo> todos = todoRepository.findAll();
        return todos.stream()
                .map(todoMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo item with id " + id + " not found"));
        todoRepository.deleteById(id);
    }

    private TodoResponseDto convertToDto(Todo todo) {
        return new TodoResponseDto(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getDueDate(),
                todo.getPriority(),
                todo.getStatus(),
                todo.getCreatedDate(),
                todo.getUpdatedDate(),
                todo.getUserId()
        );
    }

    @Override
    public TodoResponseDto findById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo item with id " + id + " not found"));

        return convertToDto(todo);
    }

    @Override
    public TodoResponseDto save(TodoCreateDto todoCreateDto) {
        Todo todo = todoMapper.toEntity(todoCreateDto);
        todo.setStatus(Status.PENDING);
        todo.setPriority(todoCreateDto.priority());
        todo.setUserId(1L);
        todo.setCreatedDate(LocalDateTime.now());
        todo.setUpdatedDate(LocalDateTime.now());
        Todo savedTodo = todoRepository.save(todo);

        // Initial history entry for creation
        TaskHistory history = new TaskHistory();
        history.setTodo(savedTodo);
        history.setOldState("N/A");
        history.setNewState(savedTodo.getStatus().toString());
        history.setChangeDate(LocalDateTime.now());
        history.setChangedBy("System");
        taskHistoryRepository.save(history);

        return todoMapper.toResponseDto(savedTodo);
    }

    @Override
    @Transactional
    public TodoResponseDto update(Long id, TodoUpdateDto todoUpdateDto) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo with id " + id + " not found."));

        String oldState = existingTodo.toString();

        todoMapper.updateEntityFromDto(todoUpdateDto, existingTodo);
        existingTodo.setUpdatedDate(LocalDateTime.now());

        if (!oldState.equals(existingTodo.toString())) {
            TaskHistory history = new TaskHistory();
            history.setTodo(existingTodo);
            history.setOldState(oldState);
            history.setNewState(existingTodo.toString());
            history.setChangeDate(LocalDateTime.now());
            history.setChangedBy("User");
            taskHistoryRepository.save(history);
        }

        Todo savedTodo = todoRepository.save(existingTodo);
        return todoMapper.toResponseDto(savedTodo);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo with id " + id + " not found."));

        todoRepository.delete(todo);

        TaskHistory history = new TaskHistory();
        history.setTodo(todo);
        history.setOldState(todo.getStatus().toString());
        history.setNewState("DELETED");
        history.setChangeDate(LocalDateTime.now());
        history.setChangedBy("User");
        taskHistoryRepository.save(history);
    }

    @Override
    public List<TaskHistoryResponseDto> findTaskHistory(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo with id " + id + " not found."));

        List<TaskHistory> historyList = taskHistoryRepository.findByTodoId(id);

        return historyList.stream()
                .map(taskHistoryMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
