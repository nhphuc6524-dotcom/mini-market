package com.minimarket.controller;

import org.springframework.web.bind.annotation.*;
import com.minimarket.repository.ProductRepository;
import com.minimarket.model.Product;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    // API lấy tất cả sản phẩm
    @GetMapping
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    // API tìm theo barcode
    @GetMapping("/barcode/{barcode}")
    public Product getByBarcode(@PathVariable String barcode){
        return productRepository.findByBarcode(barcode);
    }
}