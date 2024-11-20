package com.slobodianyk.java_spring_boot.exception;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(Long id) {
        super("Todo item with id " + id + " not found.");
    }
}
