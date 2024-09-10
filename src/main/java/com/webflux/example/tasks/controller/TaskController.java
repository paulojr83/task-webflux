package com.webflux.example.tasks.controller;


import com.webflux.example.tasks.controller.converter.TaskConverter;
import com.webflux.example.tasks.controller.request.InsertTaskRequest;
import com.webflux.example.tasks.controller.request.UpdateTaskRequest;
import com.webflux.example.tasks.controller.response.TaskResponse;
import com.webflux.example.tasks.model.Task;
import com.webflux.example.tasks.model.TaskState;
import com.webflux.example.tasks.service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("/task")
@Log4j2
public class TaskController {

    private final TaskService taskService;
    private final TaskConverter converter;

    public TaskController(TaskService taskService, TaskConverter converter) {
        this.taskService = taskService;
        this.converter = converter;
    }

    @GetMapping
    public Mono<Page<TaskResponse>> getTasks(@RequestParam(required = false) String id,
                                             @RequestParam(required = false) String title,
                                             @RequestParam(required = false) String description,
                                             @RequestParam(required = false, defaultValue = "0") int priority,
                                             @RequestParam(required = false) TaskState state,
                                             @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        Mono<Page<Task>> paginated = taskService
                .findPaginated(converter.convert(id, title, description, priority, state), pageNumber, pageSize);
        if (Objects.isNull(paginated)) {
            return Mono.just(Page.empty());
        }
        return paginated.map(tasks -> tasks.map(converter::convert));
    }

    @PostMapping
    public Mono<TaskResponse> createTask(@RequestBody @Valid InsertTaskRequest insertTaskRequest) {
        log.info("M=createTask, taskRequest={}", insertTaskRequest);
        return this.taskService
                .saveTask(converter.convert(insertTaskRequest))
                .doOnNext(task -> log.info("M=createTask, Saved task with id={}", task.getId()))
                .map(converter::convert);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTask(@PathVariable(value = "id") String id) {

        return Mono.just(id)
                .doOnNext(_id -> log.info("M=deleteTask, id={}", _id))
                .flatMap(taskService::deleteTask);
    }

    @PutMapping("/{id}")
    public Mono<TaskResponse> updateTask(@PathVariable(value = "id") String id, @RequestBody @Valid UpdateTaskRequest taskRequest) {
        return this.taskService
                .updateTask(converter.convert(id, taskRequest))
                .doOnNext(task -> log.info("M=updateTask, Updated task with id={}", id))
                .map(converter::convert);
    }
}
