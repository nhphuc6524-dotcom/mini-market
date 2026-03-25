package com.minimarket.controller;

import org.springframework.web.bind.annotation.*;

import com.minimarket.dto.InventoryReportDTO;
import com.minimarket.model.Product;
import com.minimarket.model.PurchaseOrderItem;
import com.minimarket.repository.ProductRepository;
import com.minimarket.repository.PurchaseOrderItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;
    // Doanh thu hôm nay
    @GetMapping("/today")
    public Map<String, Object> getTodayRevenue(){

        Map<String, Object> result = new HashMap<>();

        String revenueSql =
            "SELECT COALESCE(SUM(total),0) FROM orders WHERE DATE(order_date)=CURRENT_DATE";

        String orderSql =
            "SELECT COUNT(*) FROM orders WHERE DATE(order_date)=CURRENT_DATE";

        String lowStockSql =
            "SELECT COUNT(*) FROM products WHERE stock_quantity <= min_stock";

        Double revenue = jdbcTemplate.queryForObject(revenueSql, Double.class);
        Integer orderCount = jdbcTemplate.queryForObject(orderSql, Integer.class);
        Integer lowStock = jdbcTemplate.queryForObject(lowStockSql, Integer.class);

        result.put("revenue", revenue);
        result.put("orderCount", orderCount);
        result.put("lowStockCount", lowStock);

        return result;
    }

    // Doanh thu 7 ngày
    @GetMapping("/weekly")
    public List<Map<String,Object>> weeklyRevenue(){
        String sql = """
            SELECT DATE(order_date) as day, SUM(total) as revenue
            FROM orders
            WHERE order_date >= CURRENT_DATE - INTERVAL '7 day'
            GROUP BY DATE(order_date)
            ORDER BY day
        """;

        return jdbcTemplate.queryForList(sql);
    }

    // Top sản phẩm bán chạy
    @GetMapping("/top-products")
    public List<Map<String,Object>> topProducts(){
        String sql = """
            SELECT p.name, SUM(oi.quantity) as total_sold
            FROM order_items oi
            JOIN products p ON oi.product_id = p.id
            GROUP BY p.name
            ORDER BY total_sold DESC
            LIMIT 5
        """;

        return jdbcTemplate.queryForList(sql);
    }

    // Báo cáo nhập xuất tồn
    @GetMapping("/inventory")
    public List<InventoryReportDTO> inventoryReportJPA() {
        List<Product> products = productRepository.findAll();
        List<InventoryReportDTO> result = new ArrayList<>();

        for(Product p : products){
            List<PurchaseOrderItem> imports = purchaseOrderItemRepository.findByProductId(p.getId());

            int totalImported = imports.stream()
                .mapToInt(PurchaseOrderItem::getQuantity)
                .sum();

            int sold = 0; // Query tổng số đã bán nếu cần

            List<InventoryReportDTO.ImportRecord> importRecords = imports.stream()
                .map(i -> new InventoryReportDTO.ImportRecord(
                    i.getPurchaseOrder().getOrderDate(),
                    i.getQuantity(),
                    i.getCostPrice()
                ))
                .toList();

            result.add(new InventoryReportDTO(
                p.getId(),
                p.getName(),
                p.getBarcode(),
                totalImported,
                sold,
                p.getStockQuantity(),
                importRecords
            ));
        }

        return result;
    }

    // Báo cáo tồn kho
    @GetMapping("/stock")
    public List<Map<String,Object>> stockReport(){

        String sql = """
            SELECT 
                p.id,
                p.name,
                p.barcode,
                c.name AS category,
                p.price,
                p.cost_price,
                p.stock_quantity,
                p.min_stock
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.id
            ORDER BY p.stock_quantity ASC
        """;

        return jdbcTemplate.queryForList(sql);
    }

    @GetMapping("/revenue")
    public List<Map<String,Object>> revenue(
            @RequestParam(defaultValue = "day") String type){

        String sql;

        if(type.equals("month")){
            sql = """
                SELECT 
                    TO_CHAR(order_date,'MM/YYYY') AS label,
                    SUM(total) AS revenue
                FROM orders
                GROUP BY label
                ORDER BY label
                LIMIT 12
            """;
        }else{
            sql = """
                SELECT 
                    TO_CHAR(order_date,'DD/MM') AS label,
                    SUM(total) AS revenue
                FROM orders
                WHERE order_date >= CURRENT_DATE - INTERVAL '7 day'
                GROUP BY label
                ORDER BY label
            """;
        }

        return jdbcTemplate.queryForList(sql);
    }
}