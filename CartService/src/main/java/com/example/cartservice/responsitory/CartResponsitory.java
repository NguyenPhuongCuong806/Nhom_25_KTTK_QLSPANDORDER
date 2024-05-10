package com.example.cartservice.responsitory;

import com.example.cartservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartResponsitory extends JpaRepository<Cart,Long> {
}
