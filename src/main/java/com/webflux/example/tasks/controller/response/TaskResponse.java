package com.webflux.example.tasks.controller.response;

import com.webflux.example.tasks.model.TaskState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {

    private String id;
    private String title;
    private String description;
    private int priority;
    private TaskState state;
}
