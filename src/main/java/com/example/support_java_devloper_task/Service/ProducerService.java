package com.example.support_java_devloper_task.Service;

import com.example.support_java_devloper_task.Model.DTO.NewProducerDTO;
import com.example.support_java_devloper_task.Model.DTO.NewProductDTO;
import com.example.support_java_devloper_task.Model.DTO.ProducerDTO;
import com.example.support_java_devloper_task.Model.Entity.Producer;
import com.example.support_java_devloper_task.Model.Entity.Product;
import com.example.support_java_devloper_task.Repository.ProducerRepository;
import com.example.support_java_devloper_task.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProducerService {

    private final ProducerRepository producerRepository;
    private final ProductService productService;

    public ProducerService(ProducerRepository producerRepository,
                           ProductService productService) {
        this.producerRepository = producerRepository;
        this.productService = productService;
    }

    public List<ProducerDTO> getProducers() {
        List<Producer> producers = producerRepository.findAll();
        List<Long> ids = producers.stream()
                .map(Producer::getId)
                .toList();
        List<Product> products = productService.getAllProducts(ids);

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

    @Transactional
    public void addProducer(NewProducerDTO newProducerDTO) {
        Producer producer = new Producer(newProducerDTO.getProducerName());
        Producer savedProducer = producerRepository.save(producer);
        if(newProducerDTO.getProducts() != null) {
            NewProductDTO newProductDTO =
                    new NewProductDTO(
                            savedProducer.getId(),
                            newProducerDTO.getProducts());
            productService.addProduct(newProductDTO);
        }
    }
}
