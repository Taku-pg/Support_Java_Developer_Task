package com.example.support_java_devloper_task.Model.DTO;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public class NewProductDTO {
    @NotNull(message = "You have to specify producer id")
    private Long producerId;
    @NotNull(message = "You have to set at least 1 product")
    @NotEmpty(message = "You have to set at least 1 product")
    private List<Map<String, Object>> products;

    public NewProductDTO() {}

    public NewProductDTO(Long producerId, List<Map<String, Object>> products) {
        this.producerId = producerId;
        this.products = products;
    }

    public Long getProducerId() {
        return producerId;
    }

    public void setProducerId(Long producerId) {
        this.producerId = producerId;
    }

    public List<Map<String, Object>> getProducts() {
        return products;
    }

    public void setProducts (List<Map<String, Object>> products) {
        this.products = products;
    }
}
