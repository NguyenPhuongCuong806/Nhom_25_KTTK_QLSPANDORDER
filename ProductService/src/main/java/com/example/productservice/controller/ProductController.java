package com.example.productservice.controller;

import com.example.productservice.model.Product;
import com.example.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> createProduct(@RequestBody Product product){
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PostMapping(value = "/update",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateProduct(@RequestBody Product product){
        return ResponseEntity.ok(productService.updateProduct(product));
    }

    @PostMapping(value = "/find-by-id",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> findProductById(Long productId){
        return ResponseEntity.ok(productService.findProductById(productId));
    }

}
