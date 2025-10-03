package com.example.asm_java5.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    int id;
    String name;
    String rank;
    Double price;
    String image;
}
