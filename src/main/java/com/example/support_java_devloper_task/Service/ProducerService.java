package com.example.support_java_devloper_task.Service;

import com.example.support_java_devloper_task.Model.DTO.ProducerDTO;
import com.example.support_java_devloper_task.Model.Entity.Producer;
import com.example.support_java_devloper_task.Model.Entity.Product;
import com.example.support_java_devloper_task.Repository.ProducerRepository;
import com.example.support_java_devloper_task.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProducerService {

    private final ProducerRepository producerRepository;
    private final ProductRepository productRepository;

    public ProducerService(ProducerRepository producerRepository,
                           ProductRepository productRepository) {
        this.producerRepository = producerRepository;
        this.productRepository = productRepository;
    }

    public List<ProducerDTO> getProducers() {
        List<Producer> producers = producerRepository.findAll();
        List<Long> ids = producers.stream()
                .map(Producer::getId)
                .toList();
        List<Product> products = productRepository.findAllByIdIn(ids);

        final Map<Long, ProducerDTO> producerMap = new HashMap<>();

        for(Producer producer : producers) {
            if(!producerMap.containsKey(producer.getId())) {
                producerMap.put(producer.getId(),
                        new ProducerDTO(producer.getId(), producer.getProducerName()));
            }

            final List<Product> producerProducts = products.stream()
                    .filter(product -> Objects.equals(product.getProducer().getId(), producer.getId()))
                    .toList();

            producerMap.get(producer.getId()).setProducerProducts(producerProducts);
        }

        return new ArrayList<>(producerMap.values());
    }
}
