package com.example.asm_java5.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "Transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @ManyToOne
    @JoinColumn(name = "Username")
    private Account account;

    private String type; // DEPOSIT hoặc WITHDRAW
    private Double amount;
    private String paymentMethod; // Momo, Banking, Paypal,...
    private String transactionCode;
    private String status = "PENDING";

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
}

