package com.example.asm_java5.controller;

import com.example.asm_java5.entities.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class Home {
    @RequestMapping("/home")
    public String home() {
        return "homeViews/home";
    }
    @RequestMapping("/login")
    public String login() {
        return "homeViews/login";
    }
    @RequestMapping("/register")
    public String register() {
        return "homeViews/register";
    }
    @RequestMapping("/login/in")
    public String loginIn(Model model, @RequestParam("username") String username, @RequestParam("password") String password) {
        if (username.equals("admin") && password.equals("123")) {
            return "redirect:/home";
        }
        return "redirect:/login";
    }
}
