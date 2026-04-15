package com.example.support_java_devloper_task.Service;

import com.example.support_java_devloper_task.Model.DTO.NewProducerDTO;
import com.example.support_java_devloper_task.Model.DTO.NewProductDTO;
import com.example.support_java_devloper_task.Model.DTO.ProducerDTO;
import com.example.support_java_devloper_task.Model.Entity.Producer;
import com.example.support_java_devloper_task.Model.Entity.Product;
import com.example.support_java_devloper_task.Repository.ProducerRepository;
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

    /**
     * Retrieve all producers with their products.
     * If they do not have any product, empty list will be contained
     * @return List of producer DTO
     * */
    public List<ProducerDTO> getProducers() {
        List<Producer> producers = producerRepository.findAll();
        List<Product> products = productService.getAllProducts();

        final Map<Long, ProducerDTO> producerMap = new HashMap<>();

        for(Producer producer : producers) {
            if(!producerMap.containsKey(producer.getId())) {
                producerMap.put(producer.getId(),
                        new ProducerDTO(producer.getId(), producer.getProducerName()));
            }

            final List<Product> producerProducts = products.stream()
                    .filter(product ->
                            Objects.equals(
                                    product.getProducer().getId()
                                    , producer.getId()
                            )
                    )
                    .toList();

            producerMap.get(producer.getId())
                    .setProducerProducts(producerProducts);
        }

        return new ArrayList<>(producerMap.values());
    }

    /**
     * Create new producer.
     * @param newProducerDTO DTO to create new producer.
     *                       If it does not contain product field, only producer will be saved.
     * */
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

    /**
     * Update producer name
     * @param id Target producer id to update
     * @param newProducerName New producer name
     * @throws NoSuchElementException Thrown when producer is not found given id
     * */
    public void updateProducer(Long id, String newProducerName) {
        Producer producer = producerRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("producer not found"));

        producer.setProducerName(newProducerName);
        producerRepository.save(producer);
    }

    @Transactional
    public void deleteProducer(Long id) {
        producerRepository.deleteById(id);
    }
}
