package com.example.cartservice.controller;

import com.example.cartservice.model.Cart;
import com.example.cartservice.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    
    @Value("${my.url.connect}")
    private String urlConnect;

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> createCart(@RequestBody Cart cart){
        return ResponseEntity.ok(cartService.createCart(cart));
    }

    @PutMapping(value = "/update",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateCart(@RequestBody Cart cart){
        return ResponseEntity.ok(cartService.updateCart(cart));
    }
    @PostMapping(value = "/add-product")
    public ResponseEntity<?> addProduct(@RequestParam("productId") Long productId
            ,@RequestParam("quantity") Long quantity,
                                           HttpServletRequest servletRequest
                                           ) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = urlConnect;
        String inputheader = servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if (inputheader == null || !inputheader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
                if (response.getStatusCodeValue() == 200) {
                    Long userId = Long.parseLong(response.getBody());
                    return ResponseEntity.ok(cartService.addProductinCart(productId, quantity,userId));
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("not authentication");
            }catch (Exception exception){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("please login");
            }

        }
    }

    @PostMapping(value = "/delete-product-in-cart")
    public ResponseEntity<?> deleteProduct(@RequestParam("productId") Long productId
            ,@RequestParam("quantity") Long quantity,
                                           HttpServletRequest servletRequest
    ) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = urlConnect;
        String inputheader = servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if (inputheader == null || !inputheader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
                if (response.getStatusCodeValue() == 200) {
                    Long userId = Long.parseLong(response.getBody());
                    return ResponseEntity.ok(cartService.deleteProductinCart(productId, quantity,userId));
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("not authentication");
            }catch (Exception exception){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("please login");

            }

        }
    }

    @GetMapping(value = "/find-by-id/{id}")
    public ResponseEntity<Cart> findCartById(@PathVariable("id") Long cartId){
        return ResponseEntity.ok(cartService.findCartById(cartId));
    }
    @GetMapping(value = "/find-all-by-customerId/{id}")
    public ResponseEntity<?> findAllCartByCustomerId(@PathVariable("id") Long customerId,
                                                              HttpServletRequest servletRequest
                                                              ){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = urlConnect;
        String inputheader = servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if (inputheader == null || !inputheader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
                if (response.getStatusCodeValue() == 200) {
                    Long userId = Long.parseLong(response.getBody());
                    return ResponseEntity.ok(cartService.findAllCartByCustomerId(customerId));
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("please login");

            }

        }
    }

}
