package com.example.cartservice.controller;

import com.example.cartservice.model.Cart;
import com.example.cartservice.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> createCart(@RequestBody Cart cart){
        return ResponseEntity.ok(cartService.createCart(cart));
    }

    @PutMapping(value = "/update",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateCart(@RequestBody Cart cart){
        return ResponseEntity.ok(cartService.updateCart(cart));
    }

    @PostMapping(value = "/add-product")
    public ResponseEntity<Cart> addProduct(@RequestParam("productId") Long productId
            ,@RequestParam("quantity") Long quantity,
                                           HttpServletRequest servletRequest
                                           ) {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8080/api/user/check-jwt";
        String inputheader = servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if (inputheader == null || !inputheader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
            if (response.getBody() != null) {
                Long userId = Long.parseLong(response.getBody());
                return ResponseEntity.ok(cartService.addProductinCart(productId, quantity,userId));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PostMapping(value = "/delete-product-in-cart")
    public ResponseEntity<Cart> deleteProduct(@RequestParam("productId") Long productId
            ,@RequestParam("quantity") Long quantity,
                                           HttpServletRequest servletRequest
    ) {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8080/api/user/check-jwt";
        String inputheader = servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if (inputheader == null || !inputheader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
            if (response.getBody() != null) {
                Long userId = Long.parseLong(response.getBody());
                return ResponseEntity.ok(cartService.deleteProductinCart(productId, quantity,userId));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping(value = "/find-by-id/{id}")
    public ResponseEntity<Cart> findCartById(@PathVariable("id") Long cartId){
        return ResponseEntity.ok(cartService.findCartById(cartId));
    }


}
