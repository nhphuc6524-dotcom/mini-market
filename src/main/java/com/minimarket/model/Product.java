package com.minimarket.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String barcode;

    private BigDecimal price;

    @Column(name="stock_quantity")
    private Integer stockQuantity;

    @Column(name="category_id")
    private Integer categoryId;

    public Product(){}

    public Integer getId(){ return id; }
    public void setId(Integer id){ this.id=id; }

    public String getName(){ return name; }
    public void setName(String name){ this.name=name; }

    public String getBarcode(){ return barcode; }
    public void setBarcode(String barcode){ this.barcode=barcode; }

    public BigDecimal getPrice(){ return price; }
    public void setPrice(BigDecimal price){ this.price=price; }

    public Integer getStockQuantity(){ return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity){ this.stockQuantity=stockQuantity; }

    public Integer getCategoryId(){ return categoryId; }
    public void setCategoryId(Integer categoryId){ this.categoryId=categoryId; }
}