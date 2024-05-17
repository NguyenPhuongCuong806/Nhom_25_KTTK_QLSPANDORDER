package com.example.cartservice.responsitory;

import com.example.cartservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartResponsitory extends JpaRepository<Cart,Long> {
    Optional<Cart> findCartByCustomerIdAndProductId(Long customerId,Long productId);
}
