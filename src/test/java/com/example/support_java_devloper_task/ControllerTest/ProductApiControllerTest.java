package com.example.support_java_devloper_task.ControllerTest;

import com.example.support_java_devloper_task.Controller.ProductApiController;
import com.example.support_java_devloper_task.Model.DTO.ProductDTO;
import com.example.support_java_devloper_task.Service.ProductService;
import com.github.fge.jsonpatch.JsonPatchException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ProductApiController.class)
public class ProductApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    public void getAllProductsTest() throws Exception {
        Map<String, Object> attributes = Map.ofEntries(
                Map.entry("name", "telephone"),
                Map.entry("price", "1000")
        );
        ProductDTO productDTO = new ProductDTO(1L, "test producer", attributes);

        when(productService.getAllProductDTOs()).thenReturn(List.of(productDTO));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].attributes.name").value("telephone"));
    }

    @Test
    public void postProductTest_Success() throws Exception {
        String json = """
                      {
                        "producerId": 1,
                        "products": [
                            {
                                "name": "test product",
                                "price": "1000"
                            }
                        ]
                      }
                      """;
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).addProduct(any());
    }

    @Test
    public void postProductTest_Fail_Invalid_RequestBody() throws Exception {
        String json = """
                      {
                        "products": [
                            {
                                "name": "test product",
                                "price": "1000"
                            }
                        ]
                      }
                      """;
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(productService, times(0)).addProduct(any());
    }

    @Test
    public void patchProductTest_Success() throws Exception {
        String json = """
                    [
                        {
                            "op": "add",
                            "path": "/name",
                            "value": "test product"
                        }
                    ]
        """;

        mockMvc.perform(patch("/api/v1/products/{id}", 1L)
                    .contentType("application/json-patch+json")
                    .content(json))
                .andExpect(status().isNoContent());
        verify(productService, times(1)).updateProduct(any(),any());
    }

    @Test
    public void patchProductTest_Fail_ThrowException() throws Exception {
        doThrow(JsonPatchException.class).when(productService).updateProduct(any(),any());
        String json = """
                    [
                        {
                            "op": "add",
                            "path": "/name",
                            "value": "test product"
                        }
                    ]
        """;

        mockMvc.perform(patch("/api/v1/products/{id}", 1L)
                        .contentType("application/json-patch+json")
                        .content(json))
                .andExpect(status().isInternalServerError());
        verify(productService, times(1)).updateProduct(any(),any());
    }

    @Test
    public void patchProductTest_Fail_Invalid_JsonPatch() throws Exception {
        doThrow(JsonPatchException.class).when(productService).updateProduct(any(),any());
        String json = """
                    [
                        {
                            "path": "/name",
                            "value": "test product"
                        }
                    ]
        """;

        mockMvc.perform(patch("/api/v1/products/{id}", 1L)
                        .contentType("application/json-patch+json")
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(productService, times(0)).updateProduct(any(),any());
    }
}
