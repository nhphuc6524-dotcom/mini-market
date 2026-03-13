package com.minimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.minimarket.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findByBarcode(String barcode);

}