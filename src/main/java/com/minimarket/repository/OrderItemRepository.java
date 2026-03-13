package com.minimarket.repository;

import com.minimarket.model.OrderItem;
import com.minimarket.dto.OrderItemDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer>{

    @Query("""
    SELECT new com.minimarket.dto.OrderItemDTO(
        p.name,
        oi.price,
        oi.quantity
    )
    FROM OrderItem oi
    JOIN Product p ON oi.productId = p.id
    WHERE oi.orderId = :id
    """)
    List<OrderItemDTO> getOrderItems(@Param("id") Integer id);
    @Modifying
    @Transactional
    @Query("DELETE FROM OrderItem oi WHERE oi.orderId = :id")
    void deleteByOrderId(@Param("id") Integer id);

}