package com.slobodianyk.java_spring_boot.repository;

import com.slobodianyk.java_spring_boot.model.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {
    List<TaskHistory> findByTodoId(Long todoId);
}
