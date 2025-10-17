package com.example.asm_java5.repository;

import com.example.asm_java5.entities.Order;
import com.example.asm_java5.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    // Lấy chi tiết theo đơn hàng
    List<OrderDetail> findByOrder(Order order);
}

