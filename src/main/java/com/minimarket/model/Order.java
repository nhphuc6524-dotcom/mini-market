package com.minimarket.model;
import java.util.List;  // phải import List

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="order_date")
    private LocalDateTime orderDate;

    @OneToMany
    @Transient  // nếu không muốn JPA quản lý trực tiếp, dùng Transient và lưu qua repository riêng
    private List<OrderItem> items;
    @Column(name="subtotal")
    private BigDecimal subtotal;

    @Column(name="discount")
    private BigDecimal discount;

    @Column(name="payment_method")
    private String paymentMethod;

    @Column(name="total")
    private BigDecimal total;
    @Column(name="user_id")
    private Integer userId;

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Order(){}
    
    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public LocalDateTime getOrderDate(){
        return orderDate;
    }

public List<OrderItem> getItems() { return items; }
public void setItems(List<OrderItem> items) { this.items = items; }
    public void setOrderDate(LocalDateTime orderDate){
        this.orderDate = orderDate;
    }

    public BigDecimal getTotal(){
        return total;
    }

    public void setTotal(BigDecimal total){
        this.total = total;
    }
    public BigDecimal getSubtotal() {
    return subtotal;
}

public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
}

public BigDecimal getDiscount() {
    return discount;
}

public void setDiscount(BigDecimal discount) {
    this.discount = discount;
}
public String getPaymentMethod() {
    return paymentMethod;
}

public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
}
}