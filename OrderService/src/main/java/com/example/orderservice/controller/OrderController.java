package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.services.OrderServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderServices orderServices;

    @PostMapping(value = "/create/{id}")
    public ResponseEntity<Order> createOrder(@PathVariable("id") Long userId, HttpServletRequest servletRequest) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8080/api/user/check-jwt";
        String inputheader = servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if (inputheader == null || !inputheader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
            if (response.getBody() != null) {
                return ResponseEntity.ok(orderServices.createOrder(userId,servletRequest));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping(value = "/paid/{id}")
    public ResponseEntity<Order> paidOrder(@PathVariable("id") Long orderId,
                                             HttpServletRequest servletRequest){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8080/api/user/check-jwt";
        String inputheader = servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if (inputheader == null || !inputheader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
            if (response.getBody() != null) {
                return ResponseEntity.ok(orderServices.paidOrder(orderId));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping(value = "/unpaid/{id}")
    public ResponseEntity<Order> uppaidOrder(@PathVariable("id") Long orderId,
                                               HttpServletRequest servletRequest
                                               ){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8080/api/user/check-jwt";
        String inputheader = servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if (inputheader == null || !inputheader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
            if (response.getBody() != null) {
                return ResponseEntity.ok(orderServices.unpaidOrder(orderId));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PostMapping(value = "/find-by-id/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> findOrderById(@PathVariable("id") Long orderId,
                                               HttpServletRequest servletRequest
                                               ){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8080/api/user/check-jwt";
        String inputheader = servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if (inputheader == null || !inputheader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
            if (response.getBody() != null) {
                return ResponseEntity.ok(orderServices.findOrderById(orderId));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
