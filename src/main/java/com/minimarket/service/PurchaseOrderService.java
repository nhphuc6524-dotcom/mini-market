    package com.minimarket.service;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Objects;

    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import com.minimarket.dto.PurchaseItemRequest;
    import com.minimarket.dto.PurchaseOrderRequest;
    import com.minimarket.model.Product;
    import com.minimarket.model.PurchaseOrder;
    import com.minimarket.model.PurchaseOrderItem;
    import com.minimarket.model.Supplier;
    import com.minimarket.repository.ProductRepository;
    import com.minimarket.repository.PurchaseOrderItemRepository;
    import com.minimarket.repository.PurchaseOrderRepository;
    import com.minimarket.repository.SupplierRepository;

    @Service
    public class PurchaseOrderService {

        private final PurchaseOrderRepository purchaseOrderRepository;
        private final PurchaseOrderItemRepository itemRepository;
        private final ProductRepository productRepository;
        private final SupplierRepository supplierRepository;

        public PurchaseOrderService(
                PurchaseOrderRepository purchaseOrderRepository,
                PurchaseOrderItemRepository itemRepository,
                ProductRepository productRepository,
                SupplierRepository supplierRepository) {

            this.purchaseOrderRepository = purchaseOrderRepository;
            this.itemRepository = itemRepository;
            this.productRepository = productRepository;
            this.supplierRepository = supplierRepository;
        }
        @Transactional
public void createPurchaseOrder(PurchaseOrderRequest request) {

    List<PurchaseItemRequest> items = request.getItems();
    if (items == null || items.isEmpty()) {
        throw new IllegalArgumentException("Chưa có sản phẩm để nhập");
    }

    // đảm bảo supplierId không null
    Integer supplierId = Objects.requireNonNull(request.getSupplierId(), "Supplier ID không được null");

    // Lấy nhà cung cấp chung
    Supplier supplier = supplierRepository.findById(supplierId)
            .orElseThrow(() -> new IllegalArgumentException("Supplier not found: " + supplierId));

    // Tạo phiếu nhập
    PurchaseOrder order = new PurchaseOrder();
    order.setSupplier(supplier);
    order.setOrderDate(LocalDateTime.now());
    order = purchaseOrderRepository.save(order);

    double total = 0;

    for (PurchaseItemRequest item : items) {
        Integer productId = Objects.requireNonNull(item.getProductId(), "Product ID không được null");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        int qty = item.getQty() != null ? item.getQty() : 0;
        double cost = item.getCost() != null ? item.getCost() : 0.0;

        if (qty <= 0) throw new IllegalArgumentException("Số lượng phải >0");
        if (cost <= 0) throw new IllegalArgumentException("Giá nhập phải >0");

        PurchaseOrderItem orderItem = new PurchaseOrderItem();
        orderItem.setPurchaseOrder(order);
        orderItem.setProduct(product);
        orderItem.setSupplier(supplier);
        orderItem.setQuantity(qty);
        orderItem.setCostPrice(cost);

        itemRepository.save(orderItem);

        // Cập nhật tồn kho
        int newStock = (product.getStockQuantity() != null ? product.getStockQuantity() : 0) + qty;
        product.setStockQuantity(newStock);
        productRepository.save(product);

        total += qty * cost;
    }

    order.setTotal(total);
    purchaseOrderRepository.save(order);
}


}