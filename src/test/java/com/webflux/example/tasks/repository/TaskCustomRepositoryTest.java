package com.webflux.example.tasks.repository;

import com.webflux.example.tasks.TestUtils;
import com.webflux.example.tasks.model.Task;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskCustomRepositoryTest {

    @InjectMocks
    private TaskCustomRepository repository;

    @Mock
    private ReactiveMongoOperations mongoOperations;

    @Test
    void customRepository_mustReturnPageWithOneElement_whenSendTask() {
        Task task = TestUtils.getTask();
        when(mongoOperations.find(any(), eq(Task.class))).thenReturn(Flux.just(task));
        when(mongoOperations.count(any(),  eq(Task.class))).thenReturn(Mono.just(1L));
        Integer page = 0;
        Integer size = 10;
        Mono<Page<Task>> paginated = repository.findPaginated(task, page, size);
        assertNotNull(paginated);
        assertEquals(1, Objects.requireNonNull(paginated.block()).getTotalElements());
    }
}