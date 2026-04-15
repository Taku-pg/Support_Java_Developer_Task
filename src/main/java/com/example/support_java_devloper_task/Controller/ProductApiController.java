package com.example.support_java_devloper_task.Controller;

import com.example.support_java_devloper_task.Model.DTO.NewProductDTO;
import com.example.support_java_devloper_task.Model.DTO.ProductDTO;
import com.example.support_java_devloper_task.Service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/products")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(NoSuchElementException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(DataAccessException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * Retrieve list of all products with their producer name
     * */
    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProductDTOs());
    }

    /**
     * Create new products
     * @param newProductDTO DTO to create new product.
     *                      It allows to set multiple products at once
     * */
    @PostMapping()
    public ResponseEntity<?> createProduct(@RequestBody @Valid NewProductDTO newProductDTO) {
        productService.addProduct(newProductDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update partial modification
     * @param id Target product id to modify
     * @param jsonPatch Json type value which contains attributes to add/replace/remove
     * */
    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody JsonPatch jsonPatch) {
        try{
            productService.updateProduct(id,jsonPatch);
        }catch (JsonPatchException | JsonProcessingException e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete product
     * @param id Target product id to delete
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
