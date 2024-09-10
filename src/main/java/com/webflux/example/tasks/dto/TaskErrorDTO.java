package com.webflux.example.tasks.dto;

import com.webflux.example.tasks.model.Task;
import lombok.Data;

@Data
public class TaskErrorDTO extends Task {

    public TaskErrorDTO(Throwable throwable) {
        this.throwable = throwable;
    }

    private Throwable throwable;

}
