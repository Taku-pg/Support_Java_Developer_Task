package com.example.support_java_devloper_task.ServiceTest;

import com.example.support_java_devloper_task.Model.DTO.NewProducerDTO;
import com.example.support_java_devloper_task.Model.Entity.Producer;
import com.example.support_java_devloper_task.Repository.ProducerRepository;
import com.example.support_java_devloper_task.Service.ProducerService;
import com.example.support_java_devloper_task.Service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProducerServiceTest {
    @Mock
    private ProducerRepository producerRepository;
    @Mock
    private ProductService productService;
    @InjectMocks
    private ProducerService producerService;

    @Test
    public void addProducerTest_Success() throws Exception {
        NewProducerDTO mockProducerDTO = new NewProducerDTO();
        mockProducerDTO.setProducerName("test");
        Map<String, Object> product = Map.ofEntries(
                Map.entry("name", "telephone"),
                Map.entry("price", "1000")
        );
        mockProducerDTO.setProducts(List.of(product));

        Producer mockProducer = new Producer();
        mockProducer.setId(1L);

        when(producerRepository.save(any())).thenReturn(mockProducer);

        producerService.addProducer(mockProducerDTO);
        ArgumentCaptor<Producer> captor = ArgumentCaptor.forClass(Producer.class);
        verify(producerRepository).save(captor.capture());

        Producer producer = captor.getValue();
        assertEquals("test",  producer.getProducerName());
        verify(productService).addProduct(any());
    }

    @Test
    public void updateProducerTest_Success() throws Exception {
        Producer mockProducer = new Producer();
        mockProducer.setId(1L);
        mockProducer.setProducerName("test");

        when(producerRepository.findById(1L)).thenReturn(Optional.of(mockProducer));

        producerService.updateProducer(1L, "newName");
        ArgumentCaptor<Producer> captor = ArgumentCaptor.forClass(Producer.class);
        verify(producerRepository).save(captor.capture());

        Producer producer = captor.getValue();
        assertEquals("newName",  producer.getProducerName());
    }

    @Test
    public void updateProducerTest_Fail_NotFoundProduct() throws Exception {
        when(producerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                ()-> producerService.updateProducer(1L, "newName"));
    }
}
