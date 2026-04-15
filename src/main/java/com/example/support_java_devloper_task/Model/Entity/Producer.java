package com.example.support_java_devloper_task.Model.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Producer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String producerName;
    public Producer() {}

    public Producer(String producerName) {
        this.producerName = producerName;
    }

    public Long getId() {
        return id;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }
}
