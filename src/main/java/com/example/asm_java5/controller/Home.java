package com.example.asm_java5.controller;

import com.example.asm_java5.entities.Account;
import com.example.asm_java5.entities.Product;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String register(Model model, @ModelAttribute("account") Account account) {
        return "homeViews/register";
    }
    @PostMapping("/register/in")
    public String register(@Valid @ModelAttribute("account") Account account,
                           Errors errors,
                           Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("message", "Vui lòng sửa các lỗi sau!");
            return "homeViews/register";
        }

        model.addAttribute("message", "Đăng kí thành công! Vui lòng đi tới trang đăng nhập");
        model.addAttribute("account", new Account());
        return "homeViews/register";
    }


    @PostMapping("/login/in")
    public String loginIn(Model model,
                          @RequestParam("username") String username,
                          @RequestParam("password") String password,
                          RedirectAttributes redirectAttributes,
                          HttpSession session) {

        Account account = null;

        if (username.equals("admin") && password.equals("123")) {
            account = new Account();
            account.setUsername("admin");
            account.setEmail("admin");
            account.setPassword("123");
            account.setRole("admin"); // role admin
        } else if (username.equals("user") && password.equals("123")) {
            account = new Account();
            account.setUsername("user");
            account.setEmail("user");
            account.setPassword("123");
            account.setRole("buyer");
        }

        if (account != null) {
            session.setAttribute("account", account);
            return "redirect:/home";
        }

        redirectAttributes.addFlashAttribute("message", "Sai thông tin đăng nhập!");
        return "redirect:/login";
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }
}
