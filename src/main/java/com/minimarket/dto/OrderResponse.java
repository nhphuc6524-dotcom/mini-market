package com.minimarket.dto;

import com.minimarket.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponse {
    private Integer id;
    private BigDecimal total;
    private BigDecimal discount;
    private String paymentMethod;
    private LocalDateTime orderDate;
    private String username;

    public OrderResponse(Order order, String username) {
        this.id = order.getId();
        this.total = order.getTotal();
        this.discount = order.getDiscount();
        this.paymentMethod = order.getPaymentMethod();
        this.orderDate = order.getOrderDate();
        this.username = username;
    }

    // Getter & Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}