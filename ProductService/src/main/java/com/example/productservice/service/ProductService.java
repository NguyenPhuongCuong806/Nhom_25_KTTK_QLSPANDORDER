package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.reponsitory.ProductReponsitory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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

    public Product updateProduct(Product product){
        Optional<Product> optionalProduct = productReponsitory.findById(product.getId());
        if(!optionalProduct.isEmpty()) {
            RestTemplate restTemplate = new RestTemplate();
            String urlFind
                    = "http://localhost:8081/api/catches/product/find-by-id";
            try {
                Product productCatches = restTemplate.getForObject(urlFind + "/" + product.getId(), Product.class);
                if (productCatches != null) {
                    String addCatchesUrl = "http://localhost:8081/api/catches/product/update-by-id";
                    restTemplate.postForObject(addCatchesUrl, product,String.class);
                    return productReponsitory.save(product);
                }
            } catch (HttpClientErrorException.NotFound e) {
                return productReponsitory.save(product);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Product findProductById(Long productId) throws JsonProcessingException {
        Optional<Product> productOptional = productReponsitory.findById(productId);
        if(!productOptional.isEmpty()) {
            RestTemplate restTemplate = new RestTemplate();
            String urlFind
                    = "http://localhost:8081/api/catches/product/find-by-id";
            try {
                Product productCatches = restTemplate.getForObject(urlFind + "/" + productId, Product.class);
                if (productCatches != null) {
                    return productCatches;
                }
            } catch (HttpClientErrorException.NotFound e) {
                String addCatchesUrl = "http://localhost:8081/api/catches/product/create-product";
                Product product = productOptional.get();
                restTemplate.postForObject(addCatchesUrl, product,String.class);
                return product;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<Product> findAllProduct(){
        return productReponsitory.findAll();
    }
}
