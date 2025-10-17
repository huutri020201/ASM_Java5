package com.example.asm_java5.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "Username")
    private Account username;

    private Double totalAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate = new Date();

    private String status = "PENDING"; // PENDING, PAID, CANCELED

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @OneToOne(mappedBy = "order")
    private Payment payment;
}

