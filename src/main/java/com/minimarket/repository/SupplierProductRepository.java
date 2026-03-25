package com.minimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.minimarket.model.Product;
import com.minimarket.model.SupplierProduct;

import java.util.List;

public interface SupplierProductRepository extends JpaRepository<SupplierProduct, Long> {

    List<SupplierProduct> findBySupplierId(Integer supplierId);

    void deleteBySupplierId(Integer supplierId);

    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO supplier_products (supplier_id, product_id)
    VALUES (:supplierId, :productId)
    """, nativeQuery = true)
    void insertSupplierProduct(
        @Param("supplierId") Integer supplierId,
        @Param("productId") Integer productId
    );

    @Query("""
    SELECT sp.productId 
    FROM SupplierProduct sp 
    WHERE sp.supplierId = :supplierId
    """)
    List<Integer> getProductIdsBySupplierId(@Param("supplierId") Integer supplierId);

    @Query("""
    SELECT p
    FROM Product p
    JOIN SupplierProduct sp ON p.id = sp.productId
    WHERE sp.supplierId = :supplierId
    """)
    List<Product> getProductsBySupplier(@Param("supplierId") Integer supplierId);
}