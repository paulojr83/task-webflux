package com.webflux.example.tasks.service.impl;

import com.webflux.example.tasks.exception.TaskNotFoundResponse;
import com.webflux.example.tasks.model.Task;
import com.webflux.example.tasks.repository.TaskCustomRepository;
import com.webflux.example.tasks.repository.TaskRepository;
import com.webflux.example.tasks.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Log4j2
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskCustomRepository taskCustomRepository;
    private final View error;

    @Override
    public Mono<Task> saveTask(Task task) {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Mono.just(task)
                .doOnNext(_task -> log.info("M=taskRepository::save, task={}", _task))
                .map(Task::newState)
                .flatMap(this.taskRepository::save)
                .flatMap(it -> Mono.just(it))
                .doOnError(error -> log.error("M=taskRepository::save, task={}, error={}", task.getTitle(), error.getMessage()));
    }

    @Override
    public Flux<Task> getTasks() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this.taskRepository.findAll();
    }


    @Override
    public Mono<Void> deleteTask(String id) {
        return this.taskRepository.deleteById(id);
    }

    @Override
    public Mono<Page<Task>> findPaginated(Task task, Integer pageNumber, Integer pageSize) {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return taskCustomRepository.findPaginated(task, pageNumber, pageSize);
    }

    @Override
    public Mono<Task> updateTask(Task task) {
        return taskRepository.findById(task.getId())
                .map(task::update)
                .flatMap(taskRepository::save)
                .switchIfEmpty(Mono.error(TaskNotFoundResponse::new))
                .doOnError(error -> {
                    log.error("M=taskRepository::updateTask, task id={}, error={}", task.getId(), error.getMessage());
                    throw new RuntimeException("Error during update task with id=" + task.getId());
                });
    }
}
