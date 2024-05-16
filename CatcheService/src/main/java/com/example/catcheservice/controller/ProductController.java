package com.example.catcheservice.controller;

import com.example.catcheservice.modules.Product;
import com.example.catcheservice.respository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catches/product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @PostMapping(value = "/create-product", consumes = "application/json")
    public ResponseEntity<String> createUser(@RequestBody Product product) {
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.OK).body("save product catches is success!");
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Delete product in catches is success!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found product in catches!");
        }
    }

    @PutMapping("/update-by-id")
    public ResponseEntity<String> updateById(@RequestBody Product product) {
        if (productRepository.existsById(product.getId())) {
            productRepository.save(product);
            return ResponseEntity.status(HttpStatus.OK).body("update product in catches is success!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found product in catches!");
        }
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") Long id) {
        if (productRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.OK).body(productRepository.findById(id));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
