package com.example.asm_java5.controller;

import com.example.asm_java5.entities.CartItem;
import com.example.asm_java5.entities.Product;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {
    private final Map<String, CartItem> cart = new HashMap<>();

    public void add(Product product) {
        String key = product.getName();
        if (cart.containsKey(key)) {
            cart.get(key).setQuantity(cart.get(key).getQuantity() + 1);
        } else {
            cart.put(key, new CartItem(product, 1));
        }
    }

    public void remove(String name) {
        cart.remove(name);
    }

    public Map<String, CartItem> getCart() {
        return cart;
    }
    public Collection<CartItem> getAll() {
        return cart.values();
    }

    public double getTotal() {
        return cart.values().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}
