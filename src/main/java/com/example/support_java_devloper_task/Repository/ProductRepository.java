package com.example.support_java_devloper_task.Repository;

import com.example.support_java_devloper_task.Model.DTO.ProductDTO;
import com.example.support_java_devloper_task.Model.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("""
           SELECT new com.example.support_java_devloper_task.Model.DTO.ProductDTO(
                      product.id,
                      producer.producerName,
                      product.attributes
                      ) FROM Product product JOIN product.producer producer
           """)
    List<ProductDTO> getAllProductDTO();

    List<Product> findAllByIdIn(Collection<Long> ids);
}
