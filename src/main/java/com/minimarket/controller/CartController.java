package com.minimarket.controller;

import org.springframework.web.bind.annotation.*;
import com.minimarket.service.CartService;
import com.minimarket.repository.ProductRepository;
import com.minimarket.model.Product;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final ProductRepository productRepository;

    public CartController(CartService cartService,
                          ProductRepository productRepository){
        this.cartService = cartService;
        this.productRepository = productRepository;
    }

    @PostMapping("/scan/{barcode}")
    public Product scanProduct(@PathVariable String barcode){

        Product product = productRepository.findByBarcode(barcode);

        cartService.addProduct(product);

        return product;
    }

}