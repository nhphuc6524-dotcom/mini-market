package com.minimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.minimarket.model.PurchaseOrderItem;
import java.util.List;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {

    // Lấy tất cả PurchaseOrderItem theo productId
    List<PurchaseOrderItem> findByProductId(Integer productId);
}