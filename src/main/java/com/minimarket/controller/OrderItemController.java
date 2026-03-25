package com.minimarket.controller;

import com.minimarket.model.OrderItem;
import com.minimarket.model.Product;
import com.minimarket.repository.OrderItemRepository;
import com.minimarket.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
        // kiểm tra item null
        Objects.requireNonNull(item, "Order item must not be null");
        Integer productId = Objects.requireNonNull(item.getProductId(), "Product ID must not be null");
        Integer quantity = Objects.requireNonNull(item.getQuantity(), "Quantity must not be null");

        // lấy sản phẩm, ném exception nếu không tồn tại
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        // kiểm tra tồn kho
        int stockQty = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
        if(stockQty < quantity){
            throw new RuntimeException("Sản phẩm không đủ tồn kho");
        }

        // trừ tồn kho
        product.setStockQuantity(stockQty - quantity);

        productRepo.save(product);

        return repo.save(item);
    }

}