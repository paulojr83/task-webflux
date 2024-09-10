package com.webflux.example.tasks.controller.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertTaskRequest {

    @Size(min = 3, max = 60, message = "{invalid.title.Size}")
    @NotBlank(message = "{invalid.title.NotBlank}")
    private String title;


    @Size(min = 10, max = 255, message = "{invalid.description.Size}")
    @NotBlank(message = "{invalid.description.NotBlank}")
    private String description;

    @Min(value = 1, message = "{invalid.priority}")
    @Max(value = 5, message = "{invalid.priority}")
    private int priority;
}
