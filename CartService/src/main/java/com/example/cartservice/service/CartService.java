package com.example.cartservice.service;

import com.example.cartservice.model.Cart;
import com.example.cartservice.responsitory.CartResponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartResponsitory cartResponsitory;

    public boolean createCart(Cart cart){
        if(cart != null){
            cartResponsitory.save(cart);
            return true;
        }
        return false;
    }

    public boolean updateCart(Cart cart){
        Optional<Cart> optionalCart = cartResponsitory.findById(cart.getId());
        if(!optionalCart.isEmpty()) {
            cartResponsitory.save(cart);
            return true;
        }
        return false;
    }

    public Cart findCartById(Long cartId){
        Optional<Cart> cartOptional = cartResponsitory.findById(cartId);
        if(!cartOptional.isEmpty()) {
            return cartOptional.get();
        }
        return null;
    }

}
