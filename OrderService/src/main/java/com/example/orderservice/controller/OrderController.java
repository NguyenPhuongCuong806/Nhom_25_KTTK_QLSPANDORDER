package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.services.OrderServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderServices orderServices;

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> createOrder(@RequestBody Order order){
        return ResponseEntity.ok(orderServices.createOrder(order));
    }

    @PostMapping(value = "/update",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateOrder(@RequestBody Order order){
        return ResponseEntity.ok(orderServices.updateOrder(order));
    }

    @PostMapping(value = "/paid",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> paidOrder(Long orderId){
        return ResponseEntity.ok(orderServices.paidOrder(orderId));
    }

    @PostMapping(value = "/unpaid",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> uppaidOrder(Long orderId){
        return ResponseEntity.ok(orderServices.unpaidOrder(orderId));
    }

    @PostMapping(value = "/find-by-id",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> findOrderById(Long orderId){
        return ResponseEntity.ok(orderServices.findOrderById(orderId));
    }
}
