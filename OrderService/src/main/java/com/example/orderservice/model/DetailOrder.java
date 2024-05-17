package com.example.orderservice.model;

import com.example.orderservice.pks.OrderDetailPKS;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detail_order")
@Data
@IdClass(OrderDetailPKS.class)
public class DetailOrder {
    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
    @Id
    private Long productId;
    private Long quanTiTy;
    private Double price;
}
