package com.example.catcheservice.modules;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("products")
@Data
public class Product {
    @Id
    private Long id;
    private String productName;
    private Double price;
    private String description;
}
