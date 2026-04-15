package com.example.support_java_devloper_task.Controller;

import com.example.support_java_devloper_task.Model.DTO.NewProducerDTO;
import com.example.support_java_devloper_task.Model.DTO.ProducerDTO;
import com.example.support_java_devloper_task.Service.ProducerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping()
    public ResponseEntity<ProducerDTO> createProducer(@RequestBody @Valid NewProducerDTO newProducerDTO) {
        producerService.addProducer(newProducerDTO);
        return ResponseEntity.noContent().build();
    }

}
