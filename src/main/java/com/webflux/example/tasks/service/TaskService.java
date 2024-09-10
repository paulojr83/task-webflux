package com.webflux.example.tasks.service;

import com.webflux.example.tasks.model.Task;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

    Mono<Task> saveTask(Task task);
    Flux<Task> getTasks();
    Mono<Void> deleteTask(String id);
    Mono<Page<Task>> findPaginated(Task task, Integer pageNumber, Integer pageSize);
    Mono<Task>  updateTask(Task convert);
}
