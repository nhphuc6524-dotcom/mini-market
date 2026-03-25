package com.minimarket.dto;

public class PurchaseItemRequest {
    private Integer productId;
    private Integer qty;
    private Double cost;

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }
}