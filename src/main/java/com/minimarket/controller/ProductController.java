package com.minimarket.controller;

import org.springframework.web.bind.annotation.*;
import com.minimarket.repository.ProductRepository;
import com.minimarket.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
    public Product getProductById(@PathVariable Integer id){
        Integer safeId = Objects.requireNonNull(id, "ID must not be null");
        return productRepository.findById(safeId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + safeId));
    }

    // 3. Tìm theo barcode
    @GetMapping("/barcode/{barcode}")
    public Product getByBarcode(@PathVariable String barcode){
        Product p = productRepository.findByBarcode(barcode);
        if(p == null){
            throw new RuntimeException("Product not found with barcode: " + barcode);
        }
        return p;
    }

    // 4. Thêm sản phẩm
    @PostMapping
    public Product createProduct(@RequestBody Product product){
        validateProduct(product);
        
        // Đảm bảo các giá trị số không bị null
        if(product.getCostPrice() == null) product.setCostPrice(BigDecimal.ZERO);
        if(product.getPrice() == null) product.setPrice(BigDecimal.ZERO);
        if(product.getStockQuantity() == null) product.setStockQuantity(0);
        
        // Gán unitId từ dữ liệu gửi lên
        product.setUnitId(product.getUnitId());

        return productRepository.save(product);
    }

    // 5. Cập nhật sản phẩm
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Integer id, @RequestBody Product product){

        Integer safeId = Objects.requireNonNull(id, "ID must not be null");
        Product p = productRepository.findById(safeId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + safeId));

        // Cập nhật các thông tin cơ bản
        p.setName(product.getName());
        p.setBarcode(product.getBarcode());
        p.setPrice(product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO);
        p.setCostPrice(product.getCostPrice() != null ? product.getCostPrice() : BigDecimal.ZERO);
        p.setStockQuantity(product.getStockQuantity() != null ? product.getStockQuantity() : 0);
        
        // Cập nhật Danh mục và Đơn vị
        p.setCategoryId(product.getCategoryId() != null ? product.getCategoryId() : 0);
        p.setUnitId(product.getUnitId()); // ĐÂY LÀ DÒNG QUAN TRỌNG NHẤT

        return productRepository.save(p);
    }

    // 6. Xóa sản phẩm
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id){
        Integer safeId = java.util.Objects.requireNonNull(id, "ID must not be null");
        productRepository.deleteById(safeId);
    }

    // Kiểm tra dữ liệu product trước khi lưu
    private void validateProduct(Product product){
        Objects.requireNonNull(product, "Product must not be null");
        Objects.requireNonNull(product.getName(), "Product name must not be null");
        Objects.requireNonNull(product.getBarcode(), "Barcode must not be null");
    }
}