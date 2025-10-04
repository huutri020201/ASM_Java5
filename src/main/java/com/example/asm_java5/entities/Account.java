package com.example.asm_java5.entities;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Account {
    @NotBlank(message = "Chưa nhập tài khoản")
    @Size(min = 6, max = 16, message = "Tài khoản phải từ 6-16 kí tự")
    private String username;
    @NotBlank(message = "Chưa nhập email")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Chưa nhập mật khẩu")
    @Size(min = 6, max = 16, message = "Tài khoản phải từ 6-16 kí tự")
    private String password;
    private String role = "buyer";
}
