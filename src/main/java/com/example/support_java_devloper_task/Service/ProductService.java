package com.example.support_java_devloper_task.Service;

import com.example.support_java_devloper_task.Model.DTO.NewProductDTO;
import com.example.support_java_devloper_task.Model.Entity.Producer;
import com.example.support_java_devloper_task.Model.Entity.Product;
import com.example.support_java_devloper_task.Repository.ProducerRepository;
import com.example.support_java_devloper_task.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Map;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProducerRepository producerRepository;
    public ProductService(ProductRepository productRepository,
                          ProducerRepository producerRepository) {
        this.productRepository = productRepository;
        this.producerRepository = producerRepository;
    }

    @Transactional
    public void AddProduct(NewProductDTO newProductDTO){
        final Producer producer =
                producerRepository
                        .getProducerByProducerName(newProductDTO.getProducerName())
                        .orElseThrow(()-> new NoSuchElementException("producer not found"));

        final List<Map<String,Object>> products = newProductDTO.getProducts();

        products.forEach(product -> {
            if(product.isEmpty()){
                throw new IllegalArgumentException("product is empty");
            }
            final Product newProduct = new Product(product, producer);
            productRepository.save(newProduct);
        });
    }
}
