package com.minimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.minimarket.model.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long>{
}