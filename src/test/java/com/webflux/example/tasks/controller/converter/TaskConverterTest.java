package com.webflux.example.tasks.controller.converter;

import com.webflux.example.tasks.controller.request.InsertTaskRequest;
import com.webflux.example.tasks.controller.response.TaskResponse;
import com.webflux.example.tasks.model.Task;
import com.webflux.example.tasks.model.TaskState;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static com.webflux.example.tasks.TestUtils.getTask;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskConverterTest {

    @InjectMocks
    private TaskConverter converter;

    @Test
    void converter_mustReturnTaskResponse_whenInputTask() {
        Task task = getTask();

        TaskResponse convert = converter.convert(task);

        assertNotNull(convert);
        assertEquals(task.getId(), convert.getId());
        assertEquals(task.getTitle(), convert.getTitle());
        assertEquals(task.getDescription(), convert.getDescription());
        assertEquals(task.getPriority(), convert.getPriority());
    }

    @Test
    void testConvert() {
        Task task = converter.convert("1", "Title", "Description", 1, TaskState.NEW);
        assertNotNull(task);
        assertEquals("1", task.getId());
        assertEquals("Title", task.getTitle());
        assertEquals("Description", task.getDescription());
        assertEquals(1, task.getPriority());
        assertEquals(TaskState.NEW, task.getState());
    }

    @Test
    void testConvertList() {
        Task task = converter.convert("1", "Title", "Description", 1, TaskState.NEW);
        List<Task> taskList = Arrays.asList(task);
        List<TaskResponse> taskResponses = converter.convertList(taskList);
        assertNotNull(taskResponses);
        assertFalse(taskResponses.isEmpty());
        assertEquals(1, taskResponses.size());
        assertEquals(task.getId(), taskResponses.get(0).getId());
    }

    @Test
    void testConvertTaskResponse() {
        Task task = converter.convert("1", "Title", "Description", 1, TaskState.NEW);
        TaskResponse taskResponse = converter.convert(task);
        assertNotNull(taskResponse);
        assertEquals(task.getId(), taskResponse.getId());
    }

    @Test
    void testConvertTaskRequest() {
        InsertTaskRequest insertTaskRequest = new InsertTaskRequest();
        insertTaskRequest.setTitle("Title");
        insertTaskRequest.setDescription("Description");
        insertTaskRequest.setPriority(1);
        Task task = converter.convert(insertTaskRequest);
        assertNotNull(task);
        assertEquals(insertTaskRequest.getTitle(), task.getTitle());
        assertEquals(insertTaskRequest.getDescription(), task.getDescription());
        assertEquals(insertTaskRequest.getPriority(), task.getPriority());
    }
}