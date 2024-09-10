package com.webflux.example.tasks;

import com.webflux.example.tasks.model.Task;
import com.webflux.example.tasks.model.TaskState;

public class TestUtils {

    public static Task getTask() {
        Task task = Task.builder()
                .state(TaskState.NEW)
                .id("id")
                .title("title")
                .description("description")
                .priority(1)
                .build();
        return task;
    }
}
