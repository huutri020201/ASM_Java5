package com.example.asm_java5.repository;

import com.example.asm_java5.entities.Account;
import com.example.asm_java5.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // Lấy đơn hàng của 1 user
    List<Order> findByUsername(Account account);

    // Lọc theo trạng thái đơn hàng
    List<Order> findByStatus(String status);
}

