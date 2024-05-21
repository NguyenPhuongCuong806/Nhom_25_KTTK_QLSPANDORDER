package com.example.cartservice.service;

import com.example.cartservice.model.Cart;
import com.example.cartservice.responsitory.CartResponsitory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CartService {
    @Autowired
    private CartResponsitory cartResponsitory;

    @Value("${my.url.product.find-by-id}")
    private String urlProductfindById;

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

    public String addProductinCart(Long productId,Long quantity,Long userId){
        RestTemplate restTemplate = new RestTemplate();
        String urlFind
                = urlProductfindById;
        try {
            String productCatches = restTemplate.getForObject(urlFind + "/" + productId, String.class);
            ObjectMapper objectMapper = new ObjectMapper();

            if (productCatches != null) {
                try {
                    JsonNode jsonNode = objectMapper.readTree(convertToJson(productCatches));
                    long id = jsonNode.get("id").asLong();
                    double price = jsonNode.get("price").asDouble();
                    Optional<Cart> optionalCart = cartResponsitory.findCartByCustomerIdAndProductId(userId,productId);
                    if(optionalCart.isPresent()){
                        optionalCart.get().setQuanTiTy(optionalCart.get().getQuanTiTy() + quantity);
                        optionalCart.get().setPrice(optionalCart.get().getPrice() + quantity*price);
                        return cartResponsitory.save(optionalCart.get())+"";
                    }else {
                        Cart cart = new Cart();
                        cart.setProductId(id);
                        cart.setPrice(price*quantity);
                        cart.setCustomerId(userId);
                        cart.setQuanTiTy(quantity);
                        return cartResponsitory.save(cart)+"";
                    }
                }catch (Exception exception){
                    return "add fail";
                }
            }
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "add fail";
        }
        return null;
    }

    public String deleteProductinCart(Long productId,Long quantity,Long userId){
        RestTemplate restTemplate = new RestTemplate();
        String urlFind
                = urlProductfindById;
        try {
            String productCatches = restTemplate.getForObject(urlFind + "/" + productId, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            if (productCatches != null) {
                try {
                    JsonNode jsonNode = objectMapper.readTree(convertToJson(productCatches));
                    double price = jsonNode.get("price").asDouble();
                    Optional<Cart> optionalCart = cartResponsitory.findCartByCustomerIdAndProductId(userId,productId);
                    if(optionalCart.isPresent()){
                        if(quantity < optionalCart.get().getQuanTiTy()){
                            optionalCart.get().setQuanTiTy(optionalCart.get().getQuanTiTy() - quantity);
                            optionalCart.get().setPrice(optionalCart.get().getPrice() - quantity*price);
                            return cartResponsitory.save(optionalCart.get())+"";
                        }else if(quantity == optionalCart.get().getQuanTiTy()){
                            cartResponsitory.delete(optionalCart.get());
                            return "delete product success!";
                        }else {
                            return "quantity product not exactly";
                        }
                    }
                    return "not found product";
                }catch (Exception exception){
                    System.out.println(exception);
                    return "not found product";
                }
            }
        } catch (HttpClientErrorException.NotFound e) {
            return "not found product";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Cart> findAllCartByCustomerId(Long customerId){
        return cartResponsitory.findAllByCustomerId(customerId);
    }

    private static String convertToJson(String productString) {
        // Sử dụng biểu thức chính quy để phân tích cú pháp chuỗi
        Pattern pattern = Pattern.compile("Product\\(id=(\\d+), productName=([^,]+), price=([^,]+), description=(.*)\\)");
        Matcher matcher = pattern.matcher(productString);

        if (matcher.find()) {
            long id = Long.parseLong(matcher.group(1));
            String productName = matcher.group(2).trim();
            double price = Double.parseDouble(matcher.group(3));
            String description = matcher.group(4).trim();

            return String.format("{\"id\":%d,\"productName\":\"%s\",\"price\":%f,\"description\":\"%s\"}", id, productName, price, description);
        }
        throw new IllegalArgumentException("Invalid product string format");
    }

}
