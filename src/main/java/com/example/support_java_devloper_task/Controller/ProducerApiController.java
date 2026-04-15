package com.example.support_java_devloper_task.Controller;

import com.example.support_java_devloper_task.Model.DTO.ProducerDTO;
import com.example.support_java_devloper_task.Service.ProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/producers")
public class ProducerApiController {

    private final ProducerService producerService;

    public ProducerApiController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping()
    public ResponseEntity<List<ProducerDTO>> getAllProducers() {
        List<ProducerDTO> producers = producerService.getProducers();
        return ResponseEntity.ok(producers);
    }

}
