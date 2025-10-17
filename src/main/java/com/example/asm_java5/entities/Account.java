package com.example.asm_java5.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 20, message = "Tên đăng nhập phải từ 6-20 ký tự")
    @Column(length = 50)
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 20, message = "Mật khẩu phải có ít nhất 6-20 ký tự")
    @Column(nullable = false, length = 100)
    private String password;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Column(length = 100, unique = true)
    private String email;

    private Boolean role = false;  // false = user, true = admin
    private Boolean active = true;
    private Double balance = 0.0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    private String fullname;
    private String phone;
    private String googleId;

    // ========== Quan hệ ==========
    @OneToMany(mappedBy = "seller")
    private List<GameAccount> gameAccounts;

    @OneToMany(mappedBy = "username")
    private List<Order> orders;

    @OneToMany(mappedBy = "payer")
    private List<Payment> payments;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;
}
