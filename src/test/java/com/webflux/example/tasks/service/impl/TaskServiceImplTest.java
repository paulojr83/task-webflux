package com.webflux.example.tasks.service.impl;

import com.webflux.example.tasks.TestUtils;
import com.webflux.example.tasks.controller.response.TaskResponse;
import com.webflux.example.tasks.model.Task;
import com.webflux.example.tasks.repository.TaskCustomRepository;
import com.webflux.example.tasks.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    @Mock
    private TaskCustomRepository taskCustomRepository;

    @Mock
    private TaskRepository taskRepository;

    @Test
    void service_returnOk_whenSaveTaskSuccessufully() {
        Task task = TestUtils.getTask();
        when(taskRepository.save(any())).thenReturn(Mono.just(task));
        StepVerifier.create(taskServiceImpl.saveTask(task))
                .then(() -> verify(taskRepository, times(1)).save(any()))
                .expectNext(task)
                .expectComplete();
    }


    @Test
    void service_returnVoid_whenDeleteSuccessufully() {
        StepVerifier.create(taskServiceImpl.deleteTask(anyString()))
                .then(() -> verify(taskRepository,
                        times(1)).deleteById(anyString()))
                .expectComplete();
    }

    @Test
    void service_returnPageTask_whenFindPaginated() {
        Task task = TestUtils.getTask();
        when(taskCustomRepository.findPaginated(any(), anyInt(), anyInt())).thenReturn(Mono.just(Page.empty()));

        Mono<Page<Task>> result = taskServiceImpl.findPaginated(task, 0, 10);
        assertNotNull(result);
        verify(taskCustomRepository, times(1)).findPaginated(any(), anyInt(), anyInt());
    }
}