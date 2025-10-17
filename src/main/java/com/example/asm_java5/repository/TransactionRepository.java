package com.example.asm_java5.repository;

import com.example.asm_java5.entities.Account;
import com.example.asm_java5.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    // Lấy tất cả giao dịch theo tài khoản
    List<Transaction> findByAccount(Account account);

    // Lọc giao dịch theo loại (DEPOSIT / WITHDRAW)
    List<Transaction> findByType(String type);
}
