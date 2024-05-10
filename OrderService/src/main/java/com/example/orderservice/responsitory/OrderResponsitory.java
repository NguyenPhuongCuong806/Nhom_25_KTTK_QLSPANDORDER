package com.example.orderservice.responsitory;

import com.example.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderResponsitory extends JpaRepository<Order, Long> {
}
