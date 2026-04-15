package com.example.support_java_devloper_task.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public class NewProducerDTO {
    @NotNull
    @NotBlank
    private String producerName;
    private List<Map<String, Object>> products;

    public String getProducerName() {
        return producerName;
    }

    public List<Map<String, Object>> getProducts() {
        return products;
    }
}
