package com.example.orderservice.services;

import com.example.orderservice.enums.OrderEnum;
import com.example.orderservice.model.DetailOrder;
import com.example.orderservice.model.Order;
import com.example.orderservice.responsitory.DetailOrderRepository;
import com.example.orderservice.responsitory.OrderResponsitory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServices {
    @Autowired
    private OrderResponsitory orderResponsitory;
    private final DetailOrderRepository detailOrderRepository;

    public Order createOrder(Long userId, HttpServletRequest servletRequest) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        String inputheader = servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if (inputheader == null || !inputheader.startsWith("Bearer ")) {
            return null;
        } else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);

            String urlchild
                    = "http://localhost:8083/api/cart/find-all-by-customerId";
            ResponseEntity<String> response = restTemplate.exchange(
                    urlchild + "/" + userId, HttpMethod.GET, httpEntity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            if (jsonNode.isArray()) {
                for (JsonNode jsonNodes : jsonNode) {
                    ((ObjectNode) jsonNodes).remove("id");
                    ((ObjectNode) jsonNodes).remove("customerId");
                }
            }
            List<DetailOrder> orderList = objectMapper.convertValue(
                    jsonNode,
                    new TypeReference<List<DetailOrder>>() {}
            );
            double total = 0;
            for (int i =0;i<orderList.size();i++){
                total += orderList.get(i).getPrice();
            }
            try {
                Order order = new Order();
                order.setTotal(total);
                order.setCustomerId(userId);
                order.setStatus(OrderEnum.UNPAID);
                Order order1 = orderResponsitory.save(order);

                for (int i =0;i < orderList.size() ; i++){
                    orderList.get(i).setOrder(order1);
                    detailOrderRepository.save(orderList.get(i));
                }
                order1.setDetailOrders(orderList);
                return order1;
            }catch (Exception e){
                return null;
            }
        }
    }

    public boolean updateOrder(Order order){
        Optional<Order> orderOptional = orderResponsitory.findById(order.getId());
        if(!orderOptional.isEmpty()) {
            orderResponsitory.save(order);
            return true;
        }
        return false;
    }

    public Order paidOrder(Long orderId){
        Optional<Order> orderOptional = orderResponsitory.findById(orderId);
        if(!orderOptional.isEmpty()) {
            orderOptional.get().setStatus(OrderEnum.PAID);
            return orderOptional.get();
        }
        return null;
    }

    public Order unpaidOrder(Long orderId){
        Optional<Order> orderOptional = orderResponsitory.findById(orderId);
        if(!orderOptional.isEmpty()) {
            orderOptional.get().setStatus(OrderEnum.UNPAID);
            return orderOptional.get();
        }
        return null;
    }

    public Order findOrderById(Long orderId){
        Optional<Order> orderOptional = orderResponsitory.findById(orderId);
        if(!orderOptional.isEmpty()) {
            return orderOptional.get();
        }
        return null;
    }
}
