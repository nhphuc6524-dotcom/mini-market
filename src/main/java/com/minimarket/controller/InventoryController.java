package com.minimarket.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.minimarket.repository.ProductRepository;
import com.minimarket.model.Product;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final ProductRepository productRepository;

    public InventoryController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getInventory(){
        return productRepository.findAll();
    }
}