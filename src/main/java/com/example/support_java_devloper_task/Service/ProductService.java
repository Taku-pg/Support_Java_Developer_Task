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

    /**
     * Retrieve all products dto
     * @return list of product dto
     * */
    public List<ProductDTO> getAllProductDTOs() {
        return productRepository.getAllProductDTO();
    }

    /**
     * Retrieve all products
     * @return list of product entity
     * */
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    /**
     * Create new product
     * @param newProductDTO DTO for creating new product
     * @throws NoSuchElementException Thrown when producer is not found
     * @throws IllegalArgumentException Thrown when product value is empty or null
     * */
    @Transactional
    public void addProduct(NewProductDTO newProductDTO){
        final Producer producer =
                producerRepository
                        .findProducerById(newProductDTO.getProducerId())
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

    /**
     * Update partial change for product
     * @param id Target product id to update
     * @param jsonPatch json type value which contains attributes to add/replace/remove
     * @throws NoSuchElementException Thrown when target product is not found or attribute of product is null
     * @throws JsonPatchException JsonPatchException Thrown when invalid operation, path not found or type invalid
     * @throws JsonProcessingException Thrown when invalid json data structure or failed to generate json
     * */
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
        final JsonNode patched = jsonPatch.apply(objectMapper.valueToTree(attributes));
        return objectMapper.treeToValue(patched, Map.class);
    }

    @Transactional
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }
}
