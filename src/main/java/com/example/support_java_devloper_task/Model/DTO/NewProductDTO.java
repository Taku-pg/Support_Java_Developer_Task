package com.example.support_java_devloper_task.Model.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public class NewProductDTO {
    @NotNull(message = "You have to specify producer id")
    private Long id;
    @NotNull(message = "You have to set at least 1 product")
    @NotEmpty(message = "You have to set at least 1 product")
    private List<Map<String, Object>> products;

    public NewProductDTO() {}

    public NewProductDTO(Long id, List<Map<String, Object>> products) {
        this.id = id;
        this.products = products;
    }

    public Long getProducerId() {
        return id;
    }

    public List<Map<String, Object>> getProducts() {
        return products;
    }
}
