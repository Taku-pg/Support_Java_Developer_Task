package com.example.support_java_devloper_task.Model.DTO;

import com.example.support_java_devloper_task.Model.Entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProducerDTO {
    private Long id;
    private String producerName;
    private final  List<ProducerProductDTO> producerProducts = new ArrayList<>();

    public ProducerDTO(Long id, String producerName) {
        this.id = id;
        this.producerName = producerName;
    }

    public Long getId() {
        return id;
    }

    public String getProducerName() {
        return producerName;
    }

    public List<ProducerProductDTO> getProducerProducts() {
        return producerProducts;
    }

    public void setProducerProducts(final List<Product> products) {
        for (final Product product : products) {
            final ProducerProductDTO producerProductDTO =
                    new ProducerProductDTO(
                            product.getId(),
                            product.getAttributes());

            producerProducts.add(producerProductDTO);
        }

    }

    public record ProducerProductDTO(Long id, Map<String, Object> attributes) {
    }
}
