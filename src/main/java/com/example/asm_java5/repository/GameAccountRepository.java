package com.example.asm_java5.repository;

import com.example.asm_java5.entities.GameAccount;
import com.example.asm_java5.entities.GameCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameAccountRepository extends JpaRepository<GameAccount, Integer> {

    // Lấy danh sách acc theo danh mục (VD: LOL, TFT,...)
    List<GameAccount> findByCategoryCategoryIdAndSoldFalse(String categoryId);

    // Lấy danh sách acc chưa bán
    List<GameAccount> findBySoldFalse();

    // Tìm theo từ khóa trong tiêu đề
    List<GameAccount> findByTitleContainingIgnoreCase(String keyword);
}

