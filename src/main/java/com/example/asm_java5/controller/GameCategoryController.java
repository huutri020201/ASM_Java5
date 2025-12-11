package com.example.asm_java5.controller;

import com.example.asm_java5.entities.GameCategory;
import com.example.asm_java5.repository.GameCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categories")
public class GameCategoryController {

    @Autowired
    private GameCategoryRepository categoryRepo;

    @GetMapping
    public String listCategories(Model model,
                                 @RequestParam(defaultValue = "0") int page) {
        int size = 5;
        Page<GameCategory> pages = categoryRepo.findAll(PageRequest.of(page, size));

        // fix nếu page > totalPages - 1
        if(page >= pages.getTotalPages() && pages.getTotalPages() > 0){
            page = pages.getTotalPages() - 1;
            pages = categoryRepo.findAll(PageRequest.of(page, size));
        }

        model.addAttribute("categoryPage", pages);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("gameCategory", new GameCategory());

        return "adminViews/categories";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute GameCategory gameCategory) {
        categoryRepo.save(gameCategory);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model,
                       @RequestParam(defaultValue = "0") int page) {
        GameCategory category = categoryRepo.findById(id).orElse(new GameCategory());
        Page<GameCategory> pages = categoryRepo.findAll(PageRequest.of(page, 5));

        model.addAttribute("categoryPage", pages);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("gameCategory", category);

        return "adminViews/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        categoryRepo.deleteById(id);
        return "redirect:/admin/categories";
    }
}
