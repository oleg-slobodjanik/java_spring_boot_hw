package com.slobodianyk.java_spring_boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTodo() {
        TodoTestHelper testHelper = new TodoTestHelper(objectMapper);

        String requestBody = testHelper.createTodoRequest(
                "Test Title",
                "Test Description",
                LocalDateTime.now().plusDays(1),
                "MEDIUM",
                "PENDING"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/todos", request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void testUpdateTodo() {
        TodoTestHelper testHelper = new TodoTestHelper(objectMapper);

        // First, create a Todo
        String createRequestBody = testHelper.createTodoRequest(
                "Original Title",
                "Original Description",
                LocalDateTime.now().plusDays(2),
                "LOW",
                "PENDING"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> createRequest = new HttpEntity<>(createRequestBody, headers);

        ResponseEntity<String> createResponse = restTemplate.postForEntity("/todos", createRequest, String.class);

        assertThat(createResponse.getStatusCode().is2xxSuccessful()).isTrue();

        // Extract the created Todo's ID
        Long createdTodoId = extractTodoId(createResponse.getBody());

        // Prepare an update request
        String updateRequestBody = testHelper.createTodoRequest(
                "Updated Title",
                "Updated Description",
                LocalDateTime.now().plusDays(5),
                "HIGH",
                "IN_PROGRESS"
        );

        HttpEntity<String> updateRequest = new HttpEntity<>(updateRequestBody, headers);

        // Send the update request
        ResponseEntity<String> updateResponse = restTemplate.exchange(
                "/todos/" + createdTodoId,
                org.springframework.http.HttpMethod.PUT,
                updateRequest,
                String.class
        );

        assertThat(updateResponse.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void testDeleteTodo() {
        TodoTestHelper testHelper = new TodoTestHelper(objectMapper);

        // First, create a Todo
        String createRequestBody = testHelper.createTodoRequest(
                "Title to be Deleted",
                "Description to be Deleted",
                LocalDateTime.now().plusDays(3),
                "MEDIUM",
                "PENDING"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> createRequest = new HttpEntity<>(createRequestBody, headers);

        ResponseEntity<String> createResponse = restTemplate.postForEntity("/todos", createRequest, String.class);

        assertThat(createResponse.getStatusCode().is2xxSuccessful()).isTrue();

        // Extract the created Todo's ID
        Long createdTodoId = extractTodoId(createResponse.getBody());

        // Send the delete request
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/todos/" + createdTodoId,
                org.springframework.http.HttpMethod.DELETE,
                null,
                Void.class
        );

        // Assert that the delete response is successful (status code 200 OK or 204 No Content)
        assertThat(deleteResponse.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void testGetTaskHistory() {
        TodoTestHelper testHelper = new TodoTestHelper(objectMapper);

        // 1. Сначала создаем задачу
        String createRequestBody = testHelper.createTodoRequest(
                "Test Title for History",
                "Test Description for History",
                LocalDateTime.now().plusDays(3),
                "HIGH",
                "PENDING"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> createRequest = new HttpEntity<>(createRequestBody, headers);
        ResponseEntity<String> createResponse = restTemplate.postForEntity("/todos", createRequest, String.class);

        assertThat(createResponse.getStatusCode().is2xxSuccessful()).isTrue();

        Long createdTodoId = extractTodoId(createResponse.getBody());

        ResponseEntity<String> historyResponse = restTemplate.exchange(
                "/todos/" + createdTodoId + "/history",
                org.springframework.http.HttpMethod.GET,
                null,  // Нет тела запроса
                String.class
        );

        assertThat(historyResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(historyResponse.getBody()).isNotEmpty();

        try {
            String[] taskHistory = objectMapper.readValue(historyResponse.getBody(), String[].class);
            assertThat(taskHistory).isNotEmpty();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse task history", e);
        }
    }

    private Long extractTodoId(String responseBody) {
        try {
            return objectMapper.readTree(responseBody).get("id").asLong();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract Todo ID", e);
        }
    }
}
