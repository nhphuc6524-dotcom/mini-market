package com.minimarket.controller;

import com.minimarket.model.OrderItem;
import com.minimarket.model.Product;
import com.minimarket.repository.OrderItemRepository;
import com.minimarket.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-items")
@CrossOrigin
public class OrderItemController {

    @Autowired
    private OrderItemRepository repo;

    @Autowired
    private ProductRepository productRepo;

    @PostMapping
    public OrderItem create(@RequestBody OrderItem item){

        Product product = productRepo.findById(item.getProductId()).orElseThrow();

        // kiểm tra tồn kho
        if(product.getStockQuantity() < item.getQuantity()){
            throw new RuntimeException("Sản phẩm không đủ tồn kho");
        }

        // trừ tồn kho
        product.setStockQuantity(product.getStockQuantity() - item.getQuantity());

        productRepo.save(product);

        return repo.save(item);
    }

}