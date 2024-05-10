package com.example.orderservice.services;

import com.example.orderservice.enums.OrderEnum;
import com.example.orderservice.model.Order;
import com.example.orderservice.responsitory.OrderResponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServices {
    @Autowired
    private OrderResponsitory orderResponsitory;

    public boolean createOrder(Order order){
        if(order != null){
            orderResponsitory.save(order);
            return true;
        }
        return false;
    }

    public boolean updateOrder(Order order){
        Optional<Order> orderOptional = orderResponsitory.findById(order.getId());
        if(!orderOptional.isEmpty()) {
            orderResponsitory.save(order);
            return true;
        }
        return false;
    }

    public boolean paidOrder(Long orderId){
        Optional<Order> orderOptional = orderResponsitory.findById(orderId);
        if(!orderOptional.isEmpty()) {
            orderOptional.get().setStatus(OrderEnum.PAID);
            return true;
        }
        return false;
    }

    public boolean unpaidOrder(Long orderId){
        Optional<Order> orderOptional = orderResponsitory.findById(orderId);
        if(!orderOptional.isEmpty()) {
            orderOptional.get().setStatus(OrderEnum.UNPAID);
            return true;
        }
        return false;
    }

    public Order findOrderById(Long orderId){
        Optional<Order> orderOptional = orderResponsitory.findById(orderId);
        if(!orderOptional.isEmpty()) {
            return orderOptional.get();
        }
        return null;
    }
}
