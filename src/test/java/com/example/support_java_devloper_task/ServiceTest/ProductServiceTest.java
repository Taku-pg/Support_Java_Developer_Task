package com.example.support_java_devloper_task.ServiceTest;

import com.example.support_java_devloper_task.Model.DTO.NewProductDTO;
import com.example.support_java_devloper_task.Model.Entity.Producer;
import com.example.support_java_devloper_task.Model.Entity.Product;
import com.example.support_java_devloper_task.Repository.ProducerRepository;
import com.example.support_java_devloper_task.Repository.ProductRepository;
import com.example.support_java_devloper_task.Service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProducerRepository producerRepository;

    @InjectMocks
    private ProductService productService;
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void addProductTest_Success() throws Exception {
        NewProductDTO mockNewProductDTO = new NewProductDTO();
        Map<String, Object> product = Map.ofEntries(
                Map.entry("name", "telephone"),
                Map.entry("price", "1000")
        );
        mockNewProductDTO.setProducts(List.of(product));
        mockNewProductDTO.setProducerId(1L);

        Producer mockProducer = new Producer();
        mockProducer.setId(1L);

        when(producerRepository.findProducerById(1L)).thenReturn(Optional.of(mockProducer));

        productService.addProduct(mockNewProductDTO);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product capturedProduct = captor.getValue();
        assertEquals(capturedProduct.getAttributes(), product);
    }

    @Test
    public void addProductTest_Fail_Throw_NotFoundProducer() throws Exception {
        NewProductDTO mockNewProductDTO = new NewProductDTO();
        mockNewProductDTO.setProducerId(1L);
        when(producerRepository.findProducerById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                ()-> productService.addProduct(mockNewProductDTO));
    }

    @Test
    public void addProductTest_Fail_Throw_EmptyProduct() throws Exception {
        NewProductDTO mockNewProductDTO = new NewProductDTO();
        mockNewProductDTO.setProducerId(1L);
        mockNewProductDTO.setProducts(List.of(Map.of()));
        Producer mockProducer = new Producer();
        mockProducer.setId(1L);
        when(producerRepository.findProducerById(1L)).thenReturn(Optional.of(mockProducer));

        assertThrows(IllegalArgumentException.class,
                ()-> productService.addProduct(mockNewProductDTO));
    }

    @Test
    public void updateProductTest_Success() throws Exception {
        String json = """
                      [
                        {
                            "op": "replace",
                            "path": "/name",
                            "value": "new telephone"
                        }
                      ]
                      """;
        JsonPatch jsonPatch = JsonPatch.fromJson(objectMapper.readTree(json));

        Map<String, Object> product = Map.ofEntries(
                Map.entry("name", "telephone"),
                Map.entry("price", "1000")
        );
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setAttributes(product);

        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        productService.updateProduct(1L, jsonPatch);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product capturedProduct = captor.getValue();
        assertEquals("new telephone", capturedProduct.getAttributes().get("name"));
        assertEquals("1000", capturedProduct.getAttributes().get("price"));

    }

    @Test
    public void updateProductTest_Fail_NotFoundProduct() throws Exception {
        String json = """
                      [
                        {
                            "op": "add",
                            "path": "/name",
                            "value": "new telephone"
                        }
                      ]
                      """;
        JsonPatch jsonPatch = JsonPatch.fromJson(objectMapper.readTree(json));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                ()->productService.updateProduct(1L, jsonPatch));
    }

    @Test
    public void updateProductTest_Fail_NullAttribute() throws Exception {
        String json = """
                      [
                        {
                            "op": "add",
                            "path": "/name",
                            "value": "new telephone"
                        }
                      ]
                      """;
        JsonPatch jsonPatch = JsonPatch.fromJson(objectMapper.readTree(json));

        Product mockProduct = new Product();
        mockProduct.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        assertThrows(NoSuchElementException.class,
                ()->productService.updateProduct(1L, jsonPatch));
    }

    @Test
    public void updateProductTest_Fail_InvalidPatch() throws Exception {
        String json = """
                      [
                        {
                            "op": "add",
                            "path": "/unknown/name",
                            "value": "new telephone"
                        }
                      ]
                      """;
        JsonPatch jsonPatch = JsonPatch.fromJson(objectMapper.readTree(json));

        Map<String, Object> product = Map.ofEntries(
                Map.entry("name", "telephone"),
                Map.entry("price", "1000")
        );
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setAttributes(product);

        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        assertThrows(JsonPatchException.class,
                ()->productService.updateProduct(1L, jsonPatch));
    }
}

