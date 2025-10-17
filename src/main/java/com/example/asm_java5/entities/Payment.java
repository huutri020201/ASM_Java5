package com.example.asm_java5.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "Payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @OneToOne
    @JoinColumn(name = "OrderId")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "Payer")
    private Account payer;

    private Double amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate = new Date();

    private String paymentStatus = "SUCCESS"; // SUCCESS, FAILED
}
