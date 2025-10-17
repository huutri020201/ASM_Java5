package com.example.asm_java5.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "GameAccounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gameAccId;

    @ManyToOne
    @JoinColumn(name = "CategoryId")
    private GameCategory category;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String description;

    private String usernameGame;
    private String passwordGame;
    private String rank;
    private Double price;
    private String thumbnail;
    private Boolean sold = false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @ManyToOne
    @JoinColumn(name = "Seller")
    private Account seller;
}
