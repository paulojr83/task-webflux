package com.webflux.example.tasks.exception;

public class TaskNotFoundResponse extends RuntimeException {

    public TaskNotFoundResponse() {
        super("Task not found!");

    }
}
