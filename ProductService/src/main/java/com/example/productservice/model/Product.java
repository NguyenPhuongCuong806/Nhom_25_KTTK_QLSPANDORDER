package com.example.productservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private Double price;
    private String description;
}
