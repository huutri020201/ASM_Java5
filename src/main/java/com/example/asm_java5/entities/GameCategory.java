package com.example.asm_java5.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "GameCategories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameCategory {

    @Id
    @Column(length = 5)
    private String categoryId;

    @Column(nullable = false, length = 100)
    private String categoryName;

    @Column(length = 255)
    private String description;

    private Boolean active = true;

    @OneToMany(mappedBy = "category")
    private List<GameAccount> gameAccounts;
}
