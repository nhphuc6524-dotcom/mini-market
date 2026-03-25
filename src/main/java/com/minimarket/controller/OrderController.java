package com.minimarket.controller;

import com.minimarket.model.Order;
import com.minimarket.model.OrderItem;
import com.minimarket.model.Product;
import com.minimarket.repository.OrderRepository;
import com.minimarket.repository.ProductRepository;


import com.minimarket.repository.OrderItemRepository;
import com.minimarket.dto.OrderItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Lấy tất cả hóa đơn (mới nhất lên đầu)
    @GetMapping
    public Page<Order> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAllByOrderByIdDesc(pageable);
    }
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Integer id) {
        return orderRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    // Tạo hóa đơn
    @PostMapping
@Transactional
public Order createOrder(@RequestBody Order order) {

    order.setOrderDate(LocalDateTime.now());

    // 🔹 Kiểm tra và đảm bảo userId không null
    order.setUserId(Objects.requireNonNull(order.getUserId(), "User ID must not be null"));

    BigDecimal subtotal = BigDecimal.ZERO;

    if (order.getItems() != null) {
        for (OrderItem item : order.getItems()) {
            // 🔹 Bắt buộc productId không null
            Integer productId = Objects.requireNonNull(item.getProductId(), 
                "Product ID must not be null for order item");

            Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

            int stockQty = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
            int quantity = item.getQuantity() != null ? item.getQuantity() : 0;

            if (stockQty < quantity) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ tồn kho");
            }

            product.setStockQuantity(stockQty - quantity);
            productRepo.save(product);

            subtotal = subtotal.add(item.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }
    }

    order.setSubtotal(subtotal);
    if (order.getDiscount() == null) order.setDiscount(BigDecimal.ZERO);
    order.setTotal(subtotal.subtract(order.getDiscount()));

    if(order.getPaymentMethod() == null) order.setPaymentMethod("CASH");

    Order savedOrder = orderRepository.save(order);

if (order.getItems() != null) {
    Integer orderIdObj = Objects.requireNonNull(savedOrder.getId(), "Order ID must not be null");
    int orderId = orderIdObj.intValue();
    for (OrderItem item : order.getItems()) {
        item.setOrderId(orderId);
        orderItemRepository.save(item);
    }
}

    return savedOrder;
}


    // Lấy chi tiết hóa đơn
    @GetMapping("/{id}/items")
    public List<OrderItemDTO> getItems(@PathVariable Integer id) {
        Objects.requireNonNull(id, "Order ID must not be null");

        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found: " + id);
        }

        return orderItemRepository.getOrderItems(id);
    }

    // Xóa hóa đơn
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Integer id) {
        Objects.requireNonNull(id, "Order ID must not be null");

        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found: " + id);
        }

        orderItemRepository.deleteByOrderId(id);
        orderRepository.deleteById(id);
    }
}