package com.example.cartservice.service;

import com.example.cartservice.model.Cart;
import com.example.cartservice.responsitory.CartResponsitory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
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

    public Cart addProductinCart(Long productId,Long quantity,Long userId){
        RestTemplate restTemplate = new RestTemplate();
        String urlFind
                = "http://localhost:8082/api/product/find-by-id";
        try {
            String productCatches = restTemplate.getForObject(urlFind + "/" + productId, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            if (productCatches != null) {
                try {
                    JsonNode jsonNode = objectMapper.readTree(productCatches);
                    long id = jsonNode.get("id").asLong();
                    double price = jsonNode.get("price").asDouble();
                    Optional<Cart> optionalCart = cartResponsitory.findCartByCustomerIdAndProductId(userId,productId);
                    if(optionalCart.isPresent()){
                        optionalCart.get().setQuanTiTy(optionalCart.get().getQuanTiTy() + quantity);
                        optionalCart.get().setPrice(optionalCart.get().getPrice() + quantity*price);
                        return cartResponsitory.save(optionalCart.get());
                    }else {
                        Cart cart = new Cart();
                        cart.setProductId(id);
                        cart.setPrice(price*quantity);
                        cart.setCustomerId(userId);
                        cart.setQuanTiTy(quantity);
                        return cartResponsitory.save(cart);
                    }
                }catch (Exception exception){
                    return null;
                }
            }
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cart deleteProductinCart(Long productId,Long quantity,Long userId){
        RestTemplate restTemplate = new RestTemplate();
        String urlFind
                = "http://localhost:8082/api/product/find-by-id";
        try {
            String productCatches = restTemplate.getForObject(urlFind + "/" + productId, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            if (productCatches != null) {
                try {
                    JsonNode jsonNode = objectMapper.readTree(productCatches);
                    long id = jsonNode.get("id").asLong();
                    double price = jsonNode.get("price").asDouble();
                    Optional<Cart> optionalCart = cartResponsitory.findCartByCustomerIdAndProductId(userId,productId);
                    if(optionalCart.isPresent()){
                        if(quantity < optionalCart.get().getQuanTiTy()){
                            optionalCart.get().setQuanTiTy(optionalCart.get().getQuanTiTy() - quantity);
                            optionalCart.get().setPrice(optionalCart.get().getPrice() - quantity*price);
                            return cartResponsitory.save(optionalCart.get());
                        }else if(quantity == optionalCart.get().getQuanTiTy()){
                            cartResponsitory.delete(optionalCart.get());
                            return null;
                        }else {
                            return null;
                        }
                    }
                    return null;
                }catch (Exception exception){
                    return null;
                }
            }
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
