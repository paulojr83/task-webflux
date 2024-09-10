package com.webflux.example.tasks.controller;

import com.webflux.example.tasks.TestUtils;
import com.webflux.example.tasks.controller.converter.TaskConverter;
import com.webflux.example.tasks.controller.request.InsertTaskRequest;
import com.webflux.example.tasks.controller.response.TaskResponse;
import com.webflux.example.tasks.model.Task;
import com.webflux.example.tasks.model.TaskState;
import com.webflux.example.tasks.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.mockito.Mockito.*;

@SpringBootTest
class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @Mock
    private TaskConverter taskConverter;

    WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToController(taskController).build();
    }

    @Test
    void controller_mastReturnOk_whenCreateTaskSuccessfully() {

        when(taskConverter.convert(any(Task.class))).thenReturn(new TaskResponse());
        when(taskService.saveTask(any())).thenReturn(Mono.just(new Task()));

        client.post().uri("/task")
                .bodyValue(new InsertTaskRequest())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class);
    }

    @Test
    void controller_mastReturnOk_whenGetTasksSuccessfully() {
        Task task = TestUtils.getTask();
        Page<Task> paginated = new PageImpl<>(Collections.singletonList(task), PageRequest.of(0, 10), 1);

        when(taskConverter.convert(anyString(),anyString(), anyString(), anyInt(), any(TaskState.class))).thenReturn(new Task());

        when(taskService.findPaginated(any(Task.class), anyInt(), anyInt())).thenReturn(Mono.just(paginated));

        client.get().uri(uriBuilder -> uriBuilder
                        .path("/task")
                        .queryParam("state", "NEW")
                        .queryParam("title", "Title")
                        .queryParam("description", "Description")
                        .queryParam("priority", "1")
                        .build())
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBodyList(TaskResponse.class);
    }

    @Test
    void controller__mastReturnNoContent_whenDeleteTask() {
        when(taskService.deleteTask(anyString())).thenReturn(Mono.empty());

        String taskId = "taskId";

        client.delete()
                .uri("/task/" + taskId)
                .exchange()
                .expectStatus().isNoContent();
    }
}