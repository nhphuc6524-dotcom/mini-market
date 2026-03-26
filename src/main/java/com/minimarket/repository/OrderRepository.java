package com.minimarket.repository;

import com.minimarket.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAllByOrderByOrderDateDesc(Pageable pageable);
}