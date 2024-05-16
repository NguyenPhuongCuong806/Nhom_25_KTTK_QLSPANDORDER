package com.example.productservice.controller;

import com.example.productservice.model.Product;
import com.example.productservice.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> createProduct(@RequestBody Product product,HttpServletRequest servletRequest){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8080/api/user/check-jwt";
        String inputheader =  servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if(inputheader == null || !inputheader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        }else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
            if(response.getBody() != null){
                return ResponseEntity.ok(productService.createProduct(product));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        }
    }

    @PostMapping(value = "/update",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> updateProduct(@RequestBody Product product,HttpServletRequest servletRequest){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8080/api/user/check-jwt";
        String inputheader =  servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if(inputheader == null || !inputheader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        }else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
            if(response.getBody() != null){
                return ResponseEntity.ok(productService.updateProduct(product));

            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        }
    }

    @PostMapping(value = "/find-by-id/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable("id") Long productId) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findProductById(productId));
    }

    @GetMapping(value = "/find-all")
    public ResponseEntity<List<Product>> findAllProduct(){
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAllProduct());
    }

}
