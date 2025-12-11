package com.example.asm_java5.controller;

import com.example.asm_java5.entities.*;
import com.example.asm_java5.repository.*;
import com.example.asm_java5.service.CartService;
import com.example.asm_java5.service.EmailService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private GameAccountRepository gameRepo;

    @Autowired
    private CartService cartService; // service lưu giỏ hàng theo session

    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private EmailService emailService;
    @PostMapping("/add")
    public String addToCart(@RequestParam("gameAccId") Integer gameAccId, HttpSession session) {
        GameAccount game = gameRepo.findById(gameAccId).orElse(null);
        if (game != null) {
            cartService.add(game); // logic thêm vào giỏ
        }
        return "redirect:/cart/view";
    }
    @GetMapping("/view")
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("totalPrice", cartService.getTotal());
        return "homeViews/cart";
    }
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("gameAccId") Integer gameAccId) {
        cartService.remove(gameAccId);
        return "redirect:/cart/view";
    }

    @Autowired
    AccountRepository accountRepo;

    @Autowired
    GameAccountRepository gameAccountRepo;
    @Autowired
    PaymentRepository paymentRepo;
    @Autowired
    TransactionRepository transactionRepo;
    @Transactional
    @PostMapping("/buynow")
    public String checkout(HttpSession session, RedirectAttributes redirectAttrs) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Lấy sản phẩm từ giỏ
        Collection<GameAccount> items = cartService.getItems();
        if (items.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "empty");
            return "redirect:/cart/view";
        }

        double total = cartService.getTotal();
        if (user.getBalance() < total) {
            redirectAttrs.addFlashAttribute("error", "not_enough_balance");
            return "redirect:/cart/view";
        }

        // Trừ tiền người dùng
        user.setBalance(user.getBalance() - total);
        accountRepo.save(user);

        // === Tạo Order ===
        Order order = new Order();
        order.setUsername(user);
        order.setTotalAmount(total);
        order.setStatus("PAID");

        List<OrderDetail> details = new ArrayList<>();
        for (GameAccount g : items) {
            g.setSold(true);
            gameAccountRepo.save(g);

            OrderDetail d = new OrderDetail();
            d.setOrder(order);
            d.setGameAccount(g);
            d.setPrice(g.getPrice());
            details.add(d);
        }
        order.setOrderDetails(details);
        orderRepo.save(order);

        // === Ghi lại Payment ===
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPayer(user);
        payment.setAmount(total);
        payment.setPaymentStatus("SUCCESS");
        payment.setPaymentDate(new Date());
        paymentRepo.save(payment);

        // === Ghi lại Transaction ===
        Transaction transaction = new Transaction();
        transaction.setAccount(user);
        transaction.setType("WITHDRAW");
        transaction.setAmount(total);
        transaction.setPaymentMethod("INTERNAL");
        transaction.setTransactionCode("TXN-" + System.currentTimeMillis());
        transaction.setStatus("SUCCESS");
        transaction.setCreatedAt(new Date());
        transactionRepo.save(transaction);

        // === Gửi mail tóm tắt ===
        StringBuilder html = new StringBuilder("""
        <h2>Bạn đã mua thành công các tài khoản sau:</h2>
        <ul>
    """);

        for (GameAccount g : items) {
            html.append(String.format("""
            <li>
                <b>%s</b> - %.0f VND<br>
                Tài khoản: <code>%s</code><br>
                Mật khẩu: <code>%s</code>
            </li>
            <br>
        """, g.getTitle(), g.getPrice(), g.getUsernameGame(), g.getPasswordGame()));
        }

        html.append("</ul><p>Tổng cộng: <b>")
                .append(total)
                .append(" VND</b></p>")
                .append("<p>Cảm ơn bạn đã mua hàng tại <b>VietNam EsportGame</b>!</p>")
                .append("<p>Chúc bạn chơi game vui vẻ!</p>");

        emailService.sendOrderMail(user.getEmail(), "Xác nhận đơn hàng", html.toString());

        // Clear giỏ hàng
        cartService.clear();

        // Cập nhật session balance
        session.setAttribute("user", user);

        redirectAttrs.addFlashAttribute("success", "cart_done");
        return "redirect:/cart/view";
    }

}

