package com.slobodianyk.java_spring_boot.controller;

import com.slobodianyk.java_spring_boot.dto.TaskHistoryResponseDto;
import com.slobodianyk.java_spring_boot.dto.TodoCreateDto;
import com.slobodianyk.java_spring_boot.dto.TodoResponseDto;
import com.slobodianyk.java_spring_boot.dto.TodoUpdateDto;
import com.slobodianyk.java_spring_boot.exception.TodoNotFoundException;
import com.slobodianyk.java_spring_boot.service.impl.TodoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoServiceImpl todoService;

    @Autowired
    public TodoController(TodoServiceImpl todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public TodoResponseDto create(@Valid @RequestBody TodoCreateDto todoCreateDto) {
        return todoService.save(todoCreateDto);
    }

    @GetMapping
    public List<TodoResponseDto> getAll() {
        return todoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> getById(@PathVariable Long id) {
        TodoResponseDto todoResponse = todoService.findById(id);
        if (todoResponse == null) {
            throw new TodoNotFoundException(id);
        }
        return ResponseEntity.ok(todoResponse);
    }

    @PutMapping("/{id}")
    public TodoResponseDto update(@PathVariable Long id, @Valid @RequestBody TodoUpdateDto todoUpdateDto) {
        return todoService.update(id, todoUpdateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        todoService.deleteById(id);
    }

    @GetMapping("/{id}/history")
    public List<TaskHistoryResponseDto> getTaskHistory(@PathVariable Long id) {
        return todoService.findTaskHistory(id);
    }
}
