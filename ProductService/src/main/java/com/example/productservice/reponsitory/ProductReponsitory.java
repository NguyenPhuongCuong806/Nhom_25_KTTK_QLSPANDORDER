package com.example.productservice.reponsitory;

import com.example.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReponsitory extends JpaRepository<Product, Long> {
}
