package com.minimarket.dto;

import java.time.LocalDateTime;
import java.util.List;

public class InventoryReportDTO {
    private Integer id;
    private String name;
    private String barcode;
    private Integer imported;
    private Integer sold;
    private Integer stock;
    private List<ImportRecord> imports; // ✅ mảng lịch sử nhập

    public InventoryReportDTO(Integer id, String name, String barcode, Integer imported, Integer sold, Integer stock, List<ImportRecord> imports){
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.imported = imported;
        this.sold = sold;
        this.stock = stock;
        this.imports = imports;
    }

    // getters và setters
    public Integer getId() { return id; }
    public String getName() { return name; }
    public Integer getImported() { return imported; }
    public Integer getSold() { return sold; }
    public Integer getStock() { return stock; }
    public List<ImportRecord> getImports() { return imports; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setImported(Integer imported) { this.imported = imported; }
    public void setSold(Integer sold) { this.sold = sold; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setImports(List<ImportRecord> imports) { this.imports = imports; }

    // ✅ lớp con đại diện cho 1 bản ghi nhập
    public static class ImportRecord {
        private LocalDateTime date;
        private int qty;
        private double cost;

        public ImportRecord(LocalDateTime date, int qty, double cost) {
            this.date = date;
            this.qty = qty;
            this.cost = cost;
        }

        public LocalDateTime getDate() { return date; }
        public int getQty() { return qty; }
        public double getCost() { return cost; }

        public void setDate(LocalDateTime date) { this.date = date; }
        public void setQty(int qty) { this.qty = qty; }
        public void setCost(double cost) { this.cost = cost; }
    }
}