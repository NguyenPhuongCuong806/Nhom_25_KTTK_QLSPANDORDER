package com.example.orderservice.responsitory;

import com.example.orderservice.model.DetailOrder;
import com.example.orderservice.model.Order;
import com.example.orderservice.pks.OrderDetailPKS;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailOrderRepository extends JpaRepository<DetailOrder, OrderDetailPKS> {

}
