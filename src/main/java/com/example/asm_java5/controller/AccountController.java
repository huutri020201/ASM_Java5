package com.example.asm_java5.controller;

import com.example.asm_java5.entities.Account;
import com.example.asm_java5.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepo;

    @GetMapping
    public String listAccounts(Model model,
                               @RequestParam(defaultValue = "0") int page) {
        int size = 10;
        Page<Account> pages = accountRepo.findAll(PageRequest.of(page, size));

        // Fix trang vượt quá giới hạn
        if (page >= pages.getTotalPages() && pages.getTotalPages() > 0) {
            page = pages.getTotalPages() - 1;
            pages = accountRepo.findAll(PageRequest.of(page, size));
        }

        model.addAttribute("accountPage", pages);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("account", new Account()); // cho form thêm mới

        return "adminViews/accounts";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Account account) {
        // Nếu là thêm mới → đặt ngày tạo
        if (account.getCreatedAt() == null) {
            account.setCreatedAt(new java.util.Date());
        }
        // Mật khẩu không mã hóa ở đây (nếu bạn dùng BCrypt thì nên encode trước khi save)
        accountRepo.save(account);
        return "redirect:/admin/accounts";
    }

    @GetMapping("/edit/{username}")
    public String edit(@PathVariable String username, Model model,
                       @RequestParam(defaultValue = "0") int page) {
        Account account = accountRepo.findById(username)
                .orElse(new Account());

        Page<Account> pages = accountRepo.findAll(PageRequest.of(page, 10));

        model.addAttribute("accountPage", pages);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("account", account);

        return "adminViews/accounts";
    }

    @GetMapping("/delete/{username}")
    public String delete(@PathVariable String username) {
        accountRepo.deleteById(username);
        return "redirect:/admin/accounts";
    }
}