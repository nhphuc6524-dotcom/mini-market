package com.minimarket.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Doanh thu hôm nay
    @GetMapping("/today")
    public Map<String, Object> getTodayRevenue(){
        String sql = "SELECT COALESCE(SUM(total),0) FROM orders WHERE DATE(order_date) = CURRENT_DATE";
        Double revenue = jdbcTemplate.queryForObject(sql, Double.class);

        Map<String, Object> result = new HashMap<>();
        result.put("revenue", revenue);
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
}