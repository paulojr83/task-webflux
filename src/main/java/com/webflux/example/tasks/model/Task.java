package com.webflux.example.tasks.model;


import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Task {

    @Id
    private String id;
    private String title;
    private String description;
    private int priority;
    private TaskState state;

    public Task newState() {
         return Task.builder()
                .state(TaskState.NEW)
                .title(this.title)
                .description(this.description)
                .priority(this.priority)
                .build();
    }

    public Task update(Task oldTask) {
        return this.toBuilder()
                .state(oldTask.state)
                .build();
    }
}
