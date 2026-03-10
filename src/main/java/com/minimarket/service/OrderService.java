package com.minimarket.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.minimarket.model.*;
import com.minimarket.repository.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public Order createOrder(BigDecimal total){

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }
}