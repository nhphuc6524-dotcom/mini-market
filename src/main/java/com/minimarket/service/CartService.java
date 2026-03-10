package com.minimarket.service;

import org.springframework.stereotype.Service;
import java.util.*;

import com.minimarket.model.Product;

@Service
public class CartService {

    private Map<Integer,Integer> cart = new HashMap<>();

    public void addProduct(Product product){
        cart.put(product.getId(),
        cart.getOrDefault(product.getId(),0)+1);
    }

    public Map<Integer,Integer> getCart(){
        return cart;
    }

    public void clear(){
        cart.clear();
    }
}