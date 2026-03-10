package com.minimarket.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.minimarket.model.Product;
import com.minimarket.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getByBarcode(String barcode){
        return productRepository.findByBarcode(barcode);
    }

    public void reduceStock(Product product,int qty){

        product.setStockQuantity(
            product.getStockQuantity() - qty
        );

        productRepository.save(product);
    }
}