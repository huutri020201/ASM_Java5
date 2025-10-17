package com.example.asm_java5.repository;

import com.example.asm_java5.entities.Order;
import com.example.asm_java5.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // Tìm thanh toán theo đơn hàng
    Payment findByOrder(Order order);
}
