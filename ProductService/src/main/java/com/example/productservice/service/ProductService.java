package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.reponsitory.ProductReponsitory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            Product product1 = optionalProduct.get();
            product1.setPrice(product.getPrice());
            product1.setProductName(product.getProductName());
            product1.setDescription(product.getDescription());
            try {
                Product productCatches = restTemplate.getForObject(urlFind + "/" + product1.getId(), Product.class);
                if (productCatches != null) {
                    HttpEntity<Product> httpEntity = new HttpEntity<Product>(product1);
                    String addCatchesUrl = "http://localhost:8081/api/catches/product/update-by-id";
                    HttpEntity<Product>  exchange = restTemplate.exchange(addCatchesUrl, HttpMethod.PUT,httpEntity,Product.class);
                    return productReponsitory.save(product1);
                }
            }
            catch (Exception exception){
                return productReponsitory.save(product1);
            }

        }
        return null;
    }

    public Product findProductById(Long productId) throws JsonProcessingException {
        Optional<Product> productOptional = productReponsitory.findById(productId);
        if(productOptional.isPresent()){
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
            return productOptional.get();
        }
        return null;
    }

    public List<Product> findAllProduct(){
        return productReponsitory.findAll();
    }

    public Product deleteProduct(Long productId){
        Optional<Product> productOptional = productReponsitory.findById(productId);
        if(!productOptional.isEmpty()) {
            RestTemplate restTemplate = new RestTemplate();
            String urlFind
                    = "http://localhost:8081/api/catches/product/find-by-id";
            try {
                Product productCatches = restTemplate.getForObject(urlFind + "/" + productId, Product.class);
                if (productCatches != null) {
                    HttpEntity<Product> httpEntity = new HttpEntity<Product>(productOptional.get());
                    String addCatchesUrl = "http://localhost:8081/api/catches/product/delete-by-id";
                    HttpEntity<Product>  exchange = restTemplate.exchange(addCatchesUrl + "/" + productId, HttpMethod.DELETE,httpEntity,Product.class);
                    productReponsitory.delete(productOptional.get());
                    return productOptional.get();
                }
            }
            catch (Exception exception){
                productReponsitory.delete(productOptional.get());
                return productOptional.get();
            }
        }
        return null;
    }
}
