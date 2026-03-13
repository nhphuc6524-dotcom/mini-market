package com.minimarket.controller;

import com.minimarket.model.Order;
import com.minimarket.repository.OrderRepository;
import com.minimarket.repository.OrderItemRepository;
import com.minimarket.dto.OrderItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Lấy tất cả hóa đơn (mới nhất lên đầu)
    @GetMapping
    public Page<Order> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        Pageable pageable = PageRequest.of(page, size);

        return orderRepository.findAllByOrderByIdDesc(pageable);
    }

    // Tạo hóa đơn
    @PostMapping
    public Order createOrder(@RequestBody Order order){

        // đảm bảo có ngày tạo
        order.setOrderDate(LocalDateTime.now());

        // đảm bảo total không null
        if(order.getTotal() == null){
            order.setTotal(java.math.BigDecimal.ZERO);
        }

        return orderRepository.save(order);
    }

    // Lấy chi tiết hóa đơn
    @GetMapping("/{id}/items")
    public List<OrderItemDTO> getItems(@PathVariable Integer id){
        return orderItemRepository.getOrderItems(id);
    }

    // Xóa hóa đơn
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Integer id){

        orderItemRepository.deleteByOrderId(id);

        orderRepository.deleteById(id);

    }   

}