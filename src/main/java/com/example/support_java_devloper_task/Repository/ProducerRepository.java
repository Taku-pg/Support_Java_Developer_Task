package com.example.support_java_devloper_task.Repository;

import com.example.support_java_devloper_task.Model.Entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProducerRepository extends JpaRepository<Producer,Long> {
    Optional<Producer> getProducerByProducerName(String producerName);

    Optional<Producer> findProducerById(Long id);
}
