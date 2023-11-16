package com.example.praca.exception;

/**
 * @author Daniel Lezniak
 */
public class TaskNotFoundException extends RuntimeException {
    private static final String MSG = "Task not found exception";

    public TaskNotFoundException() {
        super(MSG);
    }
}
