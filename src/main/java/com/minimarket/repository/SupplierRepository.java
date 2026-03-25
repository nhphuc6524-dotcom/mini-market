package com.minimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.minimarket.model.Supplier;

import java.util.List;
import java.util.Map;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

@Query(value = """
SELECT 
s.id,
s.name,
s.phone,
s.email,
s.address,
COUNT(sp.product_id) AS product_count
FROM suppliers s
LEFT JOIN supplier_products sp
ON s.id = sp.supplier_id
GROUP BY s.id
ORDER BY s.id
""", nativeQuery = true)
List<Map<String,Object>> getSuppliersWithProductCount();

}