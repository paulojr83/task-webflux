package com.webflux.example.tasks.controller.converter;

import com.webflux.example.tasks.controller.request.InsertTaskRequest;
import com.webflux.example.tasks.controller.request.UpdateTaskRequest;
import com.webflux.example.tasks.controller.response.TaskResponse;
import com.webflux.example.tasks.model.Task;
import com.webflux.example.tasks.model.TaskState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TaskConverter {

    public Task convert(String taskId, String title, String description, int priority, TaskState state){
        return Task.builder()
                .id(taskId)
                .title(title)
                .description(description)
                .priority(priority)
                .state(state)
                .build();
    }
    public List<TaskResponse> convertList(List<Task> tasks) {
        List<TaskResponse> list = tasks.stream().map(this::convert).collect(Collectors.toList());
        return Optional
                .ofNullable(list)
                .orElse(new ArrayList<>());
    }

    public TaskResponse convert(Task task) {
        return Optional.ofNullable(task)
                .map(source -> new TaskResponse(source.getId(),
                                source.getTitle(),
                                source.getDescription(),
                                source.getPriority(),
                                source.getState()) )
                .orElse(null);
    }

    public Task convert(InsertTaskRequest task) {

        return Optional.ofNullable(task)
                .map(source -> Task.builder()
                            .title(source.getTitle())
                            .description(source.getDescription())
                            .priority(source.getPriority())
                            .build())

                .orElse(null);
    }

    public Task convert(String taskId, String title, String description, int priority ){
        return Task.builder()
                .id(taskId)
                .title(title)
                .description(description)
                .priority(priority)
                .build();
    }

    public Task convert(String taskId, UpdateTaskRequest taskRequest) {
        return Task.builder()
                .id(taskId)
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .priority(taskRequest.getPriority())
                .build();
    }
}
