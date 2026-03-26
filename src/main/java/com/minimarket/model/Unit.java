package com.minimarket.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "units")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên đơn vị không được để trống") // Đảm bảo name không null và không chỉ chứa khoảng trắng
    @Column(nullable = false)
    private String name;

    public Unit() {}

    public Unit(String name) {
        this.name = name;
    }

    // Getter và Setter
    public Integer getId() { 
        return id; 
    }
    
    public void setId(Integer id) { 
        this.id = id; 
    }

    public @NotNull String getName() { // Thêm @NotNull ở đây giúp IDE hiểu rằng getter này luôn trả về giá trị
        return name; 
    }
    
    public void setName(@NotNull String name) { 
        this.name = name; 
    }
}