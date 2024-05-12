package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.reponsitory.ProductReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductReponsitory productReponsitory;

    public boolean createProduct(Product product){
        if(product != null){
            productReponsitory.save(product);
            return true;
        }
        return false;
    }

    public boolean updateProduct(Product product){
        Optional<Product> optionalProduct = productReponsitory.findById(product.getId());
        if(!optionalProduct.isEmpty()) {
            productReponsitory.save(product);
            return true;
        }
        return false;
    }

    public Product findProductById(Long productId){
        Optional<Product> productOptional = productReponsitory.findById(productId);
        if(!productOptional.isEmpty()) {
            return productOptional.get();
        }
        return null;
    }
}
