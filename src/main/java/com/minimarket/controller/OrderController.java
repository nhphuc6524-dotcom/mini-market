package com.minimarket.controller;

import com.minimarket.model.Order;
import com.minimarket.model.Product;
import com.minimarket.repository.OrderRepository;
import com.minimarket.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public Order createOrder(@RequestBody Order order){

        order.setTotalAmount(order.getTotalAmount());

        Order savedOrder = orderRepository.save(order);

        return savedOrder;
    }

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable Integer id){

        Optional<Product> product = productRepository.findById(id);

        return product.orElse(null);
    }

}