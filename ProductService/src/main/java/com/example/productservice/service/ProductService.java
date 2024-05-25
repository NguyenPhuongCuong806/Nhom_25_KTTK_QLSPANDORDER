package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.reponsitory.ProductReponsitory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
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

    @Value("${my.url.product.find-by-id}")
    private String urlProductfindById;

    @Value("${my.url.product.create}")
    private String urlProductcreate;

    @Value("${my.url.product.update}")
    private String urlProductupdate;

    @Value("${my.url.product.delete}")
    private String urlProductdelete;

    public boolean createProduct(Product product){
        if(product != null){
            productReponsitory.save(product);
            return true;
        }
        return false;
    }

    public String updateProduct(Product product){
        Optional<Product> optionalProduct = productReponsitory.findById(product.getId());
        if(!optionalProduct.isEmpty()) {
            RestTemplate restTemplate = new RestTemplate();
            String urlFind
                    = urlProductfindById;
            Product product1 = optionalProduct.get();
            product1.setPrice(product.getPrice());
            product1.setProductName(product.getProductName());
            product1.setDescription(product.getDescription());
            try {
                Product productCatches = restTemplate.getForObject(urlFind + "/" + product1.getId(), Product.class);
                if (productCatches != null) {
                    HttpEntity<Product> httpEntity = new HttpEntity<Product>(product1);
                    String addCatchesUrl = urlProductupdate;
                    HttpEntity<Product>  exchange = restTemplate.exchange(addCatchesUrl, HttpMethod.PUT,httpEntity,Product.class);
                    productReponsitory.save(product1);
                    return "update product success!";
                }
            }
            catch (Exception exception){
                productReponsitory.save(product1);
                return "update product success!";

            }

        }
        return "product is not exist";
    }


    public String findProductById(Long productId) throws JsonProcessingException {
        Optional<Product> productOptional = productReponsitory.findById(productId);
        if(productOptional.isPresent()){
            RestTemplate restTemplate = new RestTemplate();
            String urlFind
                    = urlProductfindById;
            try {
                Product productCatches = restTemplate.getForObject(urlFind + "/" + productId, Product.class);
                if (productCatches != null) {
                    return productCatches+"";
                }
            } catch (HttpClientErrorException.NotFound e) {
                String addCatchesUrl = urlProductcreate;
                Product product = productOptional.get();
                restTemplate.postForObject(addCatchesUrl, product,String.class);
                return product+"";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return productOptional.get()+"";
        }
        return "product is not exist";
    }

    public List<Product> findAllProduct(){
        return productReponsitory.findAll();
    }

    public String deleteProduct(Long productId){
        Optional<Product> productOptional = productReponsitory.findById(productId);
        if(!productOptional.isEmpty()) {
            RestTemplate restTemplate = new RestTemplate();
            String urlFind
                    = urlProductfindById;
            try {
                Product productCatches = restTemplate.getForObject(urlFind + "/" + productId, Product.class);
                if (productCatches != null) {
                    HttpEntity<Product> httpEntity = new HttpEntity<Product>(productOptional.get());
                    String addCatchesUrl = urlProductdelete;
                    HttpEntity<Product>  exchange = restTemplate.exchange(addCatchesUrl + "/" + productId, HttpMethod.DELETE,httpEntity,Product.class);
                    productReponsitory.delete(productOptional.get());
                    return productOptional.get()+"";
                }
            }
            catch (Exception exception){
                productReponsitory.delete(productOptional.get());
                return productOptional.get()+"";
            }
        }
        return "product is not exist";
    }
}
