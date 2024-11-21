package com.slobodianyk.java_spring_boot;

import com.slobodianyk.java_spring_boot.dto.todoEnum.Priority;
import com.slobodianyk.java_spring_boot.models.Todo;
import com.slobodianyk.java_spring_boot.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TodoRepositoryTest extends BaseTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void shouldSaveAndRetrieveTodo() {
        Todo todo = new Todo();
        todo.setTitle("Test Task");
        todo.setDescription("Description for test task");
        todo.setPriority(Priority.MEDIUM);

        Todo savedTodo = todoRepository.save(todo);

        assertThat(savedTodo).isNotNull();
        assertThat(savedTodo.getId()).isNotNull();
        assertThat(todoRepository.findById(savedTodo.getId())).isPresent();
    }
}
