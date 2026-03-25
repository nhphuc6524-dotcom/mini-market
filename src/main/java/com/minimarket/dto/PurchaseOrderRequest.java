package com.minimarket.dto;

import java.util.List;

public class PurchaseOrderRequest {
    private Integer supplierId;
    private List<PurchaseItemRequest> items;

    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }

    public List<PurchaseItemRequest> getItems() { return items; }
    public void setItems(List<PurchaseItemRequest> items) { this.items = items; }
}