package com.example.asm_java5.controller;

import com.example.asm_java5.entities.GameCategory;
import com.example.asm_java5.repository.GameCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalController {

    @Autowired
    GameCategoryRepository categoryRepo;

    @ModelAttribute("categories")
    public List<GameCategory> getCategories() {
        return categoryRepo.findAll();
    }
}
