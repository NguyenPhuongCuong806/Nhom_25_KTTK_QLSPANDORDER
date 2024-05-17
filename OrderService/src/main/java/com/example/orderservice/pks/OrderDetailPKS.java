package com.example.orderservice.pks;

import com.example.orderservice.model.Order;

import java.io.Serializable;

public class OrderDetailPKS implements Serializable {
    private Order order;
    private Long productId;
}
