package com.example.productservice.controller;

import com.example.productservice.model.Product;
import com.example.productservice.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
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

    @Value("${my.url.connect}")
    private String urlConnect;


    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;


    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProduct(@RequestBody Product product,HttpServletRequest servletRequest){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = urlConnect;
        String inputheader =  servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if(inputheader == null || !inputheader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("please login");

        }else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);

            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
                if(response.getStatusCodeValue() == 200){
                    return ResponseEntity.ok(productService.createProduct(product));
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }catch (Exception exception){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("please login");

            }
        }
    }

    @PostMapping(value = "/update",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProduct(@RequestBody Product product,HttpServletRequest servletRequest){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = urlConnect;
        String inputheader =  servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if(inputheader == null || !inputheader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("please login");

        }else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
                System.out.println("check data json"+response.getStatusCode());
                if(response.getStatusCodeValue() == 200){
                    return ResponseEntity.ok(productService.updateProduct(product));
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("please login");
            }

        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long productId,HttpServletRequest servletRequest){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = urlConnect;
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        String inputheader =  servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if(inputheader == null || !inputheader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("please login");
        }else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            try {
                return circuitBreaker.run(() -> {
                            ResponseEntity<String> response = restTemplate.exchange(
                                    fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
                            if(response.getStatusCodeValue() == 200){
                                return ResponseEntity.ok(productService.deleteProduct(productId));
                            }
                            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                        },
                        throwable -> {
                            System.out.println("check circuitBreaker");
                            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("block HTTP from circuitBreaker");
                        }
                        );
            }catch (Exception exception){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("please login");
            }


        }
    }

    @GetMapping(value = "/find-by-id/{id}")
    public ResponseEntity<?> findProductById(@PathVariable("id") Long productId) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findProductById(productId));
    }

    @GetMapping(value = "/find-all")
    public ResponseEntity<List<Product>> findAllProduct(){
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAllProduct());
    }

}
