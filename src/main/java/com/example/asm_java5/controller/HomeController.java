package com.example.asm_java5.controller;

import com.example.asm_java5.entities.*;
import com.example.asm_java5.repository.*;
import com.example.asm_java5.service.CookieService;
import com.example.asm_java5.service.EmailService;
import com.example.asm_java5.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    GameCategoryRepository categoryRepo;

    @GetMapping("/home")
    public String home() {
        return "homeViews/home";
    }

    @Autowired
    AccountRepository accountRepo;

    @Autowired
    SessionService sessionService;

    @Autowired
    CookieService cookieService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        // lấy cookie nếu có
        String username = cookieService.getValue("username");
        String password = cookieService.getValue("password");

        if (username != null && password != null) {
            model.addAttribute("username", username);
            model.addAttribute("password", password);
        }
        return "homeViews/login"; // trỏ tới file login.html
    }

    // ======== POST /login/in ========
    @PostMapping("/login/in")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "remember", required = false) String remember,
                        Model model) {

        Account acc = accountRepo.findById(username).orElse(null);

        if (acc == null) {
            model.addAttribute("message", "Tài khoản không tồn tại!");
            return "homeViews/login";
        }

        if (!acc.getPassword().equals(password)) {
            model.addAttribute("message", "Sai mật khẩu!");
            return "homeViews/login";
        }

        if (!acc.getActive()) {
            model.addAttribute("message", "Tài khoản đã bị khóa!");
            return "homeViews/login";
        }

        // Save session
        sessionService.set("user", acc);

        // Ghi nhớ đăng nhập (Remember me)
        if (remember != null) {
            cookieService.add("username", username, 24 * 10); // 24h
            cookieService.add("password", password, 24 * 10);
        } else {
            cookieService.remove("username");
            cookieService.remove("password");
        }
        return "redirect:/home";
    }

    @GetMapping("/out")
    public String logout() {
        sessionService.remove("user");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String form(Model model) {
        model.addAttribute("account", new Account());
        return "homeViews/register";
    }

    // ===== POST /register/in =====
    @PostMapping("/register/in")
    public String register(@Valid @ModelAttribute("account") Account account,
                           Errors errors,
                           @RequestParam("confirm") String confirm,
                           Model model) {

        // 1️⃣ Kiểm tra validate cơ bản
        if (errors.hasErrors()) {
            model.addAttribute("message", "Vui lòng sửa các lỗi bên dưới!");
            return "homeViews/register";
        }

        // 2️⃣ Kiểm tra xác nhận mật khẩu
        if (!account.getPassword().equals(confirm)) {
            model.addAttribute("message", "Mật khẩu xác nhận không khớp!");
            return "homeViews/register";
        }

        // 3️⃣ Kiểm tra username hoặc email trùng
        if (accountRepo.existsById(account.getUsername())) {
            model.addAttribute("message", "Tên đăng nhập đã tồn tại!");
            return "homeViews/register";
        }

        // 4️⃣ Thiết lập mặc định
        account.setActive(true);
        account.setRole(false);
        account.setBalance(0.0);

        // 5️⃣ Lưu DB
        accountRepo.save(account);

        // 6️⃣ Thông báo
        model.addAttribute("message", "Đăng ký thành công! Vui lòng đăng nhập.");
        model.addAttribute("account", new Account());
        return "homeViews/register";
    }

    @Autowired
    GameAccountRepository gameAccountRepo;

    @GetMapping("/{id}")
    public String viewCategory(@PathVariable("id") String id, Model model) {
        GameCategory category = categoryRepo.findById(id).orElse(null);
        List<GameAccount> accounts = gameAccountRepo.findByCategoryCategoryIdAndSoldFalse(id);

        model.addAttribute("category", category);
        model.addAttribute("accounts", accounts);

        return "homeViews/accCategory";
    }

    @GetMapping("/detail/{id}")
    public String viewGameDetail(@PathVariable("id") Integer id, Model model) {
        GameAccount game = gameAccountRepo.findById(id).orElse(null);

        if (game == null) {
            return "redirect:/home"; // nếu không tìm thấy game
        }

        model.addAttribute("game", game);
        List<GameAccount> relatedGames = gameAccountRepo
                .findByCategoryCategoryIdAndSoldFalse(game.getCategory().getCategoryId())
                .stream()
                .filter(g -> !g.getGameAccId().equals(game.getGameAccId()))
                .limit(3)
                .toList();

        model.addAttribute("relatedGames", relatedGames);
        return "homeViews/accDetail"; // trỏ tới file gameDetail.html
    }

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransactionRepository transactionRepo;

    @Transactional
    @PostMapping("/detail/buyNow")
    public String buyNow(@RequestParam("gameAccId") Integer gameAccId, HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        GameAccount game = gameAccountRepo.findById(gameAccId).orElse(null);
        if (game == null || game.getSold()) {
            return "redirect:/home?error=notfound";
        }

        // Kiểm tra số dư
        if (user.getBalance() < game.getPrice()) {
            return "redirect:/cart/view?error=not_enough_balance";
        }

        // Trừ tiền và cập nhật
        user.setBalance(user.getBalance() - game.getPrice());
        accountRepo.save(user);

        // Cập nhật acc đã bán
        game.setSold(true);
        gameAccountRepo.save(game);

        // Tạo Order và OrderDetail
        Order order = new Order();
        order.setUsername(user);
        order.setTotalAmount(game.getPrice());
        order.setStatus("PAID");

        OrderDetail detail = new OrderDetail();
        detail.setOrder(order);
        detail.setGameAccount(game);
        detail.setPrice(game.getPrice());

        order.setOrderDetails(List.of(detail));

        // Lưu đơn hàng (tự cascade sang OrderDetail)
        orderRepo.save(order);

        // Ghi lại Payment (nếu bạn muốn lưu lịch sử thanh toán nội bộ)
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPayer(user);
        payment.setAmount(game.getPrice());
        payment.setPaymentStatus("SUCCESS");
        payment.setPaymentDate(new Date());
        paymentRepo.save(payment);

        Transaction transaction = new Transaction();
        transaction.setAccount(user);
        transaction.setType("WITHDRAW");
        transaction.setAmount(game.getPrice());
        transaction.setPaymentMethod("INTERNAL");
        transaction.setTransactionCode("TXN-" + System.currentTimeMillis());
        transaction.setStatus("SUCCESS");
        transaction.setCreatedAt(new Date());
        transactionRepo.save(transaction);
        // === Gửi email xác nhận đơn hàng ===
        String subject = "Xác nhận mua tài khoản game #" + game.getGameAccId();

        String html = """
                <h2>Chúc mừng bạn đã mua thành công tài khoản!</h2>
                <p><b>Tài khoản game:</b> %s</p>
                <p><b>Giá:</b> %.0f VND</p>
                <p><b>Ngày mua:</b> %s</p>
                <hr>
                <p>Thông tin đăng nhập:</p>
                <ul>
                    <li><b>Username:</b> %s</li>
                    <li><b>Password:</b> %s</li>
                </ul>
                "<p>Cảm ơn bạn đã mua hàng tại <b>VietNam EsportGame</b>!</p>"
                <p>Chúc bạn chơi game vui vẻ!</p>
                """.formatted(
                game.getTitle(),
                game.getPrice(),
                order.getOrderDate(),
                game.getUsernameGame(),
                game.getPasswordGame()
        );

        emailService.sendOrderMail(user.getEmail(), subject, html);


        // Cập nhật session balance (để web hiển thị tức thì)
        session.setAttribute("user", user);

        return "redirect:/home?success=buy_done";
    }

    // Hiển thị form thông tin cá nhân
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Account account = accountRepo.findById(user.getUsername()).orElse(null);
        model.addAttribute("account", account);
        return "homeViews/profile";
    }

    // Cập nhật thông tin
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("account") Account form,
                                Model model, RedirectAttributes redirectAttributes) {
        Account user = sessionService.get("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Giữ lại các trường không hiển thị trong form
        Account acc = accountRepo.findById(user.getUsername()).orElse(null);
        if (acc != null) {
            acc.setFullname(form.getFullname());
            acc.setPhone(form.getPhone());
            acc.setEmail(form.getEmail());
            accountRepo.save(acc);

            sessionService.set("user", acc); // cập nhật session
        }
        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công");
        return "redirect:/profile";
    }

    @GetMapping("/changepass")
    public String changepass() {
        return "homeViews/changepass";
    }
    @PostMapping("/changepass")
    public String changePassword(RedirectAttributes redirectAttributes,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword) {
        Account user = sessionService.get("user");
        if (user == null) return "redirect:/login";

        Account acc = accountRepo.findById(user.getUsername()).orElse(null);
        if (acc == null) return "redirect:/login";

        // kiểm tra mật khẩu cũ
        if (!acc.getPassword().equals(currentPassword)) {
            redirectAttributes.addFlashAttribute("message", "Mật khẩu hiện tại không đúng");
            return "redirect:/changepass";
        }

        // xác nhận mật khẩu mới
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("message", "Xác nhận mật khẩu không khớp");
            return "redirect:/changepass";
        }

        acc.setPassword(newPassword); // nên mã hóa nếu có bcrypt
        accountRepo.save(acc);
        sessionService.set("user", acc);
        redirectAttributes.addFlashAttribute("successp", "Cập nhật mật khẩu thành công!");
        return "redirect:/profile";
    }

}

