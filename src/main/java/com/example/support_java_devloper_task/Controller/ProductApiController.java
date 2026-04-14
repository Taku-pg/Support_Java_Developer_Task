package com.example.support_java_devloper_task.Controller;

import com.example.support_java_devloper_task.Model.DTO.NewProductDTO;
import com.example.support_java_devloper_task.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody @Valid NewProductDTO newProductDTO) {
        productService.AddProduct(newProductDTO);
        return ResponseEntity.ok().build();
    }
}
