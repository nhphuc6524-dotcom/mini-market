package com.minimarket.controller;

import org.springframework.web.bind.annotation.*;
import com.minimarket.repository.ProductRepository;
import com.minimarket.model.Product;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    // 1. Lấy tất cả sản phẩm
    @GetMapping
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    // 2. Lấy sản phẩm theo ID
    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Integer id){
        return productRepository.findById(id);
    }

    // 3. Tìm theo barcode
    @GetMapping("/barcode/{barcode}")
    public Product getByBarcode(@PathVariable String barcode){
        return productRepository.findByBarcode(barcode);
    }

    // 4. Thêm sản phẩm
    @PostMapping
    public Product createProduct(@RequestBody Product product){
        return productRepository.save(product);
    }

    // 5. Cập nhật sản phẩm
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Integer id,
                                 @RequestBody Product product){

        Product p = productRepository.findById(id).orElseThrow();

        p.setName(product.getName());
        p.setBarcode(product.getBarcode());
        p.setPrice(product.getPrice());
        p.setStockQuantity(product.getStockQuantity());

        return productRepository.save(p);
    }

    // 6. Xóa sản phẩm
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id){
        productRepository.deleteById(id);
    }

}