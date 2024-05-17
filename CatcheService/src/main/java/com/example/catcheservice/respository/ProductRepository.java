package com.example.catcheservice.respository;

import com.example.catcheservice.modules.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {
    private static final String KEY_PREFIX = "products:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void save(Product product) {
        String key = KEY_PREFIX + product.getId();
        redisTemplate.opsForValue().set(key, product);
    }

    public Product findById(Long id) {
        String key = KEY_PREFIX + id;
        return (Product) redisTemplate.opsForValue().get(key);
    }



    public void deleteById(Long id) {
        String key = KEY_PREFIX + id;
        redisTemplate.delete(key);
    }

    public boolean existsById(Long id) {
        String key = KEY_PREFIX + id;
        return redisTemplate.hasKey(key);
    }

    public void update(Product product) {
        String key = KEY_PREFIX + product.getId();
        redisTemplate.opsForValue().set(key, product);
    }
}
