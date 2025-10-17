package com.example.asm_java5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @RequestMapping
    public String homeAdmin(){
        return "adminViews/home";
    }
    @RequestMapping("/games")
    public String productAdmin(){
        return "adminViews/games";
    }
}
