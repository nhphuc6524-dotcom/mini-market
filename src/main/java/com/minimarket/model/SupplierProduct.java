package com.minimarket.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="supplier_products")
public class SupplierProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="supplier_id")
    private Integer supplierId;

    @Column(name="product_id")
    private Integer productId;

    @Column(name="cost_price")
    private Double costPrice;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    public SupplierProduct(){}

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public Integer getSupplierId(){ return supplierId; }
    public void setSupplierId(Integer supplierId){ this.supplierId = supplierId; }

    public Integer getProductId(){ return productId; }
    public void setProductId(Integer productId){ this.productId = productId; }

    public Double getCostPrice(){ return costPrice; }
    public void setCostPrice(Double costPrice){ this.costPrice = costPrice; }

    public LocalDateTime getCreatedAt(){ return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt){ this.createdAt = createdAt; }
}