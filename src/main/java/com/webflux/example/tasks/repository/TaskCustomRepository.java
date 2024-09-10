package com.webflux.example.tasks.repository;

import com.webflux.example.tasks.model.Task;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class TaskCustomRepository {

    private final ReactiveMongoOperations mongoOperations;

    public Mono<Page<Task>> findPaginated(Task task, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("priority", "status");

        Example<Task> example = Example.of(task, matcher);

        Query query = new Query(Criteria.byExample(example)).with(pageable);

        if (task.getPriority() > 0) {
            query.addCriteria(Criteria.where("priority").is(task.getPriority()));
        }

        if (task.getState() != null) {
            query.addCriteria(Criteria.where("state").is(task.getState()));
        }

        Flux<Task> taskFlux = mongoOperations.find(query, Task.class);

        return mongoOperations.count(query, Task.class)
                .flatMap(count -> taskFlux.collectList()
                        .map(tasks -> new PageImpl<>(tasks, pageable, count)));
    }
}
