package com.example.asm_java5.repository;

import com.example.asm_java5.entities.GameCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameCategoryRepository extends JpaRepository<GameCategory, String> {

    // Tìm theo tên danh mục
    GameCategory findByCategoryName(String name);
}

