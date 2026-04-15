package com.example.support_java_devloper_task.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public class NewProducerDTO {
    @NotNull(message = "You have to specify producer name")
    @NotBlank(message = "You have to specify producer name")
    private String producerName;
    private List<Map<String, Object>> products;

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public List<Map<String, Object>> getProducts() {
        return products;
    }

    public void setProducts(List<Map<String, Object>> products) {
        this.products = products;
    }
}
