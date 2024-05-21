package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.services.OrderServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderServices orderServices;

    @Value("${my.url.connect}")
    private String urlConnect;

    @PostMapping(value = "/create")
    public ResponseEntity<?> createOrder(HttpServletRequest servletRequest) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = urlConnect;
        String inputheader = servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if (inputheader == null || !inputheader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
                if (response.getStatusCodeValue() == 200) {
                    long userId = Long.parseLong(response.getBody());
                    System.out.println(response.getBody());
                    return ResponseEntity.ok(orderServices.createOrder(userId,servletRequest));
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("not authentication");
            }catch (Exception exception){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("please login");

            }

        }
    }

    @GetMapping(value = "/paid/{id}")
    public ResponseEntity<?> paidOrder(@PathVariable("id") Long orderId,
                                             HttpServletRequest servletRequest){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = urlConnect;
        String inputheader = servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if (inputheader == null || !inputheader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
                if (response.getStatusCodeValue() == 200) {
                    return ResponseEntity.ok(orderServices.paidOrder(orderId));
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }catch (Exception exception){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("please login");

            }

        }
    }

    @GetMapping(value = "/unpaid/{id}")
    public ResponseEntity<?> uppaidOrder(@PathVariable("id") Long orderId,
                                               HttpServletRequest servletRequest
                                               ){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = urlConnect;
        String inputheader = servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if (inputheader == null || !inputheader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
                if (response.getStatusCodeValue() == 200) {
                    return ResponseEntity.ok(orderServices.unpaidOrder(orderId));
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }catch (Exception exception){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("please login");

            }

        }
    }

    @PostMapping(value = "/find-by-id/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findOrderById(@PathVariable("id") Long orderId,
                                               HttpServletRequest servletRequest
                                               ){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = urlConnect;
        String inputheader = servletRequest.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        if (inputheader == null || !inputheader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else {
            headers.set("Authorization", inputheader);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        fooResourceUrl, HttpMethod.POST, httpEntity, String.class);
                if (response.getStatusCodeValue() == 200) {
                    return ResponseEntity.ok(orderServices.findOrderById(orderId));
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }catch (Exception exception){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("please login");

            }

        }
    }
}
