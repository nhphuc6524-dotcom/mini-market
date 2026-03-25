package com.minimarket.controller;

import org.springframework.web.bind.annotation.*;

import com.minimarket.repository.SupplierProductRepository;
import com.minimarket.repository.SupplierRepository;
import com.minimarket.model.Product;
import com.minimarket.model.Supplier;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Map;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin
public class SupplierController {

    private final SupplierRepository supplierRepository;
    private final SupplierProductRepository supplierProductRepository;

    public SupplierController(
        SupplierRepository supplierRepository,
        SupplierProductRepository supplierProductRepository
    ){
        this.supplierRepository = supplierRepository;
        this.supplierProductRepository = supplierProductRepository;
    }

        @GetMapping
        public List<Map<String,Object>> getAll(){
            return supplierRepository.getSuppliersWithProductCount();
        }

    


    // lấy theo id
    @GetMapping("/{id}")
    public Map<String,Object> getById(@PathVariable Integer id){

        Integer safeId = Objects.requireNonNull(id, "ID must not be null");

        Supplier supplier = supplierRepository.findById(safeId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        List<Integer> products =
            supplierProductRepository.getProductIdsBySupplierId(safeId);

        return Map.of(
            "supplier", supplier,
            "products", products
        );
    }

    // thêm
    @PostMapping
    @Transactional
    public Supplier create(@RequestBody Map<String,Object> body){

        Supplier supplier = new Supplier();

        supplier.setName((String) body.get("name"));
        supplier.setPhone((String) body.get("phone"));
        supplier.setEmail((String) body.get("email"));
        supplier.setAddress((String) body.get("address"));

        supplier = supplierRepository.save(supplier);

        Object productsObj = body.get("products");

        if(productsObj instanceof List<?> list){

            for(Object p : list){

                Integer productId = Integer.valueOf(p.toString());

                supplierProductRepository.insertSupplierProduct(
                    supplier.getId(),
                    productId
                );

            }

        }

        return supplier;
    }

    // update
    @PutMapping("/{id}")
    @Transactional
    public Supplier update(@PathVariable Integer id,
                        @RequestBody Map<String,Object> body){

        Integer safeId = Objects.requireNonNull(id, "ID must not be null");

        Supplier s = supplierRepository.findById(safeId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        s.setName((String) body.get("name"));
        s.setPhone((String) body.get("phone"));
        s.setEmail((String) body.get("email"));
        s.setAddress((String) body.get("address"));

        supplierRepository.save(s);

        // Xóa product cũ
        supplierProductRepository.deleteBySupplierId(safeId);

        // Thêm product mới
        Object productsObj = body.get("products");

        if(productsObj instanceof List<?> list){

            for(Object p : list){

                Integer productId = Integer.valueOf(p.toString());

                supplierProductRepository.insertSupplierProduct(
                    safeId,
                    productId
                );
            }
        }

        return s;
    }

    // delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        Integer safeId = Objects.requireNonNull(id, "ID must not be null");
        supplierRepository.deleteById(safeId);
    }

    @GetMapping("/{id}/products")
    public List<Product> getProductsBySupplier(@PathVariable Integer id){
        return supplierProductRepository.getProductsBySupplier(id);
    }

}