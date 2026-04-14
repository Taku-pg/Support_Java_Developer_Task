package com.example.support_java_devloper_task.Model.DTO;

import java.util.Map;

public record ProductDTO(Long id, String producerName, Map<String, Object> attributes) {
}
