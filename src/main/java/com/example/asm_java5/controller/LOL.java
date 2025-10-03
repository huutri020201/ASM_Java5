package com.example.asm_java5.controller;

import com.example.asm_java5.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/lol")
public class LOL {

    private final List<Product> products = new ArrayList<>();
    @Autowired
    private CartService cartService;

    // Constructor khởi tạo sẵn 12 sản phẩm
    public LOL() {
        products.add(new Product(1,"Tài khoản LOL Thách Đấu", "Thách Đấu", 1500000.0, "acc.webp"));
        products.add(new Product(2,"Tài khoản LOL Đại Cao Thủ", "Đại Cao Thủ", 1200000.0, "acc.webp"));
        products.add(new Product(3,"Tài khoản LOL Cao Thủ", "Cao Thủ", 900000.0, "acc.webp"));
        products.add(new Product(4,"Tài khoản LOL Kim Cương", "Kim Cương", 600000.0, "acc.webp"));
        products.add(new Product(5,"Tài khoản LOL Bạch Kim", "Bạch Kim", 450000.0, "acc.webp"));
        products.add(new Product(6,"Tài khoản LOL Vàng", "Vàng", 300000.0, "acc.webp"));
        products.add(new Product(7,"Tài khoản LOL Bạc", "Bạc", 200000.0, "acc.webp"));
        products.add(new Product(8,"Tài khoản LOL Sắt", "Sắt", 100000.0, "acc.webp"));
        products.add(new Product(9,"Tài khoản LOL Full Skin", "Đặc biệt", 2500000.0, "acc.webp"));
        products.add(new Product(10,"Tài khoản LOL Nhiều Tướng", "Rank ngẫu nhiên", 500000.0, "acc.webp"));
        products.add(new Product(11,"Tài khoản LOL Smurf", "Rank thấp", 150000.0, "acc.webp"));
        products.add(new Product(12,"Tài khoản LOL Random", "Ngẫu nhiên", 50000.0, "acc.webp"));
    }

    // Trang danh mục
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", products);
        return "homeViews/lol";
    }
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") int id, Model model) {
        Product product = products.stream()
                .filter(p -> p.getId()== id)
                .findFirst()
                .orElse(null);

        if (product == null) {
            return "redirect:/lol";
        }

        model.addAttribute("product", product);

        List<Product> related = products.stream()
                .filter(p -> p.getId() != id)
                .limit(4)
                .toList();

        model.addAttribute("related", related);

        return "homeViews/detail";
    }
    @GetMapping({"/add/{id}", "/buy/{id}"})
    public String addToCart(@PathVariable("id") int id) {
        products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .ifPresent(cartService::add);
        return "redirect:/cart";
    }
}
