package com.example.support_java_devloper_task.Repository;

import com.example.support_java_devloper_task.Model.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
