package com.example.asm_java5.controller;

import com.example.asm_java5.entities.GameAccount;
import com.example.asm_java5.entities.GameCategory;
import com.example.asm_java5.repository.GameAccountRepository;
import com.example.asm_java5.repository.GameCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/games")
public class GameAccountController {
    @Autowired
    private GameAccountRepository gameRepo;


    @GetMapping
    public String listGameAccounts(Model model,
                                   @RequestParam(defaultValue = "0") int page) {
        int size = 5;
        Page<GameAccount> pages = gameRepo.findAll(PageRequest.of(page, size));

        // fix nếu page > totalPages - 1
        if(page >= pages.getTotalPages() && pages.getTotalPages() > 0){
            page = pages.getTotalPages() - 1;
            pages = gameRepo.findAll(PageRequest.of(page, size));
        }

        model.addAttribute("gamePage", pages);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("gameAccount", new GameAccount());

        return "adminViews/games";
    }


    @PostMapping("/save")
    public String save(@ModelAttribute GameAccount gameAccount) {
        gameRepo.save(gameAccount);
        return "redirect:/admin/games";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model,
                       @RequestParam(defaultValue = "0") int page) {
        GameAccount game = gameRepo.findById(id).orElse(new GameAccount());
        Page<GameAccount> pages = gameRepo.findAll(PageRequest.of(page, 5));

        model.addAttribute("gamePage", pages);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("gameAccount", game);

        return "adminViews/games";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        gameRepo.deleteById(id);
        return "redirect:/admin/games";
    }
}
