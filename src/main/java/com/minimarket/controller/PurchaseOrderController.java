    package com.minimarket.controller;

    import java.util.HashMap;
import java.util.List;
import java.util.Map;

    import org.springframework.web.bind.annotation.*;
import com.minimarket.repository.PurchaseOrderRepository;
    import com.minimarket.dto.PurchaseOrderRequest;
    import com.minimarket.service.PurchaseOrderService;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService,
                                   PurchaseOrderRepository purchaseOrderRepository){
        this.purchaseOrderService = purchaseOrderService;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @PostMapping
    public String create(@RequestBody PurchaseOrderRequest request){
        purchaseOrderService.createPurchaseOrder(request);
        return "Purchase order created";
    }

    @GetMapping
    public List<Map<String, Object>> listAll() {
        return purchaseOrderRepository.findAll().stream().map(order -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", order.getId());
            map.put("date", order.getOrderDate());
            map.put("total", order.getTotal());
            map.put("supplierName", order.getSupplier() != null ? order.getSupplier().getName() : "N/A");

            List<Map<String, Object>> items = order.getItems().stream().map(item -> {
                Map<String, Object> i = new HashMap<>();
                i.put("productId", item.getProduct().getId());
                i.put("productName", item.getProduct().getName());
                i.put("qty", item.getQuantity());
                i.put("cost", item.getCostPrice());
                i.put("supplierId", item.getSupplier().getId());
                i.put("supplierName", item.getSupplier().getName());
                return i;
            }).toList();

            map.put("items", items);
            return map;
        }).toList();
    }
}