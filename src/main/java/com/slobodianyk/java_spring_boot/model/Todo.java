package com.slobodianyk.java_spring_boot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slobodianyk.java_spring_boot.dto.todoEnum.Priority;
import com.slobodianyk.java_spring_boot.dto.todoEnum.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "todos")
@SQLDelete(sql = "UPDATE todos SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class Todo {

    // id: Long (Unique identifier for the Todo item)
    // title: String (Short description or title of the task)
    // description: String (Detailed description of the task)
    // dueDate: LocalDateTime (When the task is due)
    // priority: Enum (e.g., LOW, MEDIUM, HIGH. Store enum value as varchar in DB
    // status: Enum (e.g., PENDING, IN_PROGRESS, COMPLETED)
    // createdDate: LocalDateTime (When the task was created)
    // updatedDate: LocalDateTime (When the task was last updated)
    // userId: Long (The ID of the user who owns the task). Hardcode value 1 for now. We will replace it when Security is implemented.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "user_id")
    private Long userId = 1L;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskHistory> taskHistories = new ArrayList<>();

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
