package com.example.support_java_devloper_task.ControllerTest;

import com.example.support_java_devloper_task.Controller.ProducerApiController;
import com.example.support_java_devloper_task.Model.DTO.ProducerDTO;
import com.example.support_java_devloper_task.Model.Entity.Product;
import com.example.support_java_devloper_task.Service.ProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProducerApiController.class)
public class ProducerApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProducerService producerService;

    @Test
    public void getAllProducerTest() throws Exception{
        ProducerDTO producerDTO = new ProducerDTO(1L,"test");
        Map<String, Object> attributes = Map.ofEntries(
                Map.entry("name", "telephone"),
                Map.entry("price", "1000")
        );

        Product product = new Product();
        product.setAttributes(attributes);

        producerDTO.setProducerProducts(List.of(product));

        when(producerService.getProducers()).thenReturn(List.of(producerDTO));

        mockMvc.perform(get("/api/v1/producers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].producerProducts[0].attributes.name").value("telephone"));
    }

    @Test
    public void postNewProducerTest_Success() throws Exception{
        String json = """
                        {
                            "producerName": "test company",
                            "products": [
                                {
                                    "name": "test product",
                                    "color": "red"
                                }
                            ]
                        }
                        """;
        mockMvc.perform(post("/api/v1/producers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());
        verify(producerService, times(1)).addProducer(any());
    }

    @Test
    public void postNewProducerTest_Fail_Invalid_Body() throws Exception{
        String json = """
                        {
                            "products": [
                                {
                                    "name": "test product",
                                    "color": "red"
                                }
                            ]
                        }
                        """;
        mockMvc.perform(post("/api/v1/producers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(producerService, times(0)).addProducer(any());
    }

    @Test
    public void putNewProducerTest() throws Exception{
        String json = """
                        {
                            "producerName": "new test company"
                        }
                       """;

        mockMvc.perform(put("/api/v1/producers/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNoContent());
        verify(producerService, times(1)).updateProducer(any(), any());
    }
}
