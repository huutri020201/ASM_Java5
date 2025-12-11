package com.example.asm_java5.repository;

import com.example.asm_java5.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    // Tìm theo email (phục vụ login hoặc Google login)
    Optional<Account> findByEmail(String email);

    // Tìm theo GoogleId
    Optional<Account> findByGoogleId(String googleId);

    // Kiểm tra username đã tồn tại
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
