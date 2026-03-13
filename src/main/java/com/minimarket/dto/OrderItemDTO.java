package com.minimarket.dto;

import java.math.BigDecimal;

public class OrderItemDTO {

    private String productName;
    private BigDecimal price;
    private Integer quantity;

    public OrderItemDTO(String productName, BigDecimal price, Integer quantity){
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductName(){ return productName; }
    public BigDecimal getPrice(){ return price; }
    public Integer getQuantity(){ return quantity; }
}