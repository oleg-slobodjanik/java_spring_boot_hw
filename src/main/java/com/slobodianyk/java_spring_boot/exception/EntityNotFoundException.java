package com.slobodianyk.java_spring_boot.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Long id) {
        super("Todo item with id " + id + " not found.");
    }
}
