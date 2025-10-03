package com.example.asm_java5.controller;

import com.example.asm_java5.entities.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class Cart {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getAll());
        model.addAttribute("total", cartService.getTotal());
        return "homeViews/cart"; // view ở trong thư mục homeViews
    }
    @GetMapping("/cart/remove/{name}")
    public String remove(@PathVariable String name, Model model) {
        cartService.remove(name);
        return "redirect:/cart";
    }
}