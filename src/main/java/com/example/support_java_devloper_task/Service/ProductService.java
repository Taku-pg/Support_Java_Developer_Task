package com.example.support_java_devloper_task.Service;

import com.example.support_java_devloper_task.Model.DTO.NewProductDTO;
import com.example.support_java_devloper_task.Model.DTO.ProductDTO;
import com.example.support_java_devloper_task.Model.Entity.Producer;
import com.example.support_java_devloper_task.Model.Entity.Product;
import com.example.support_java_devloper_task.Repository.ProducerRepository;
import com.example.support_java_devloper_task.Repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Map;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProducerRepository producerRepository;
    private final ObjectMapper objectMapper;

    public ProductService(ProductRepository productRepository,
                          ProducerRepository producerRepository,
                          ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.producerRepository = producerRepository;
        this.objectMapper = objectMapper;
    }

    public List<ProductDTO> getAllProducts() {

        return productRepository.getAllProductDTO();
    }

    @Transactional
    public void AddProduct(NewProductDTO newProductDTO){
        final Producer producer =
                producerRepository
                        .getProducerByProducerName(newProductDTO.getProducerName())
                        .orElseThrow(()-> new NoSuchElementException("producer not found"));

        final List<Map<String,Object>> products = newProductDTO.getProducts();

        products.forEach(product -> {
            if(product==null || product.isEmpty()){
                throw new IllegalArgumentException("product is empty");
            }
            final Product newProduct = new Product(product, producer);
            productRepository.save(newProduct);
        });
    }

    public void updateProduct(final Long id, final JsonPatch jsonPatch) throws JsonPatchException, JsonProcessingException {
        final Product product = productRepository
                .findById(id)
                .orElseThrow(()-> new NoSuchElementException("product not found"));

        Map<String,Object> attributes = product.getAttributes();
        if(attributes==null){
            throw new NoSuchElementException("attributes is empty");
        }

        final Map<String, Object> patchedAttribute = applyPatchToAttribute(jsonPatch,attributes);
        product.setAttributes(patchedAttribute);
        productRepository.save(product);
    }

    private Map applyPatchToAttribute(final JsonPatch jsonPatch,
                                    final Map<String,Object> attributes)
            throws JsonPatchException, JsonProcessingException {
        final JsonNode patched = jsonPatch.apply(objectMapper.convertValue(attributes, JsonNode.class));
        return objectMapper.treeToValue(patched, Map.class);
    }

    @Transactional
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }
}
