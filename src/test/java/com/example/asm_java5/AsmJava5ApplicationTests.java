package com.example.asm_java5;

import com.example.asm_java5.entities.GameAccount;
import com.example.asm_java5.repository.GameAccountRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AsmJava5ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameAccountRepository gameRepo;

    // ========================== LOGIN (6 TEST) ==========================
    @Test @Order(1)
    void login_success() throws Exception {
        mockMvc.perform(post("/login/in")
                        .param("username", "admin")
                        .param("password", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test @Order(2)
    void login_wrongPassword() throws Exception {
        mockMvc.perform(post("/login/in")
                        .param("username", "admin")
                        .param("password", "sai123"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sai mật khẩu!")));
    }

    @Test @Order(3)
    void login_nonExistAccount() throws Exception {
        mockMvc.perform(post("/login/in")
                        .param("username", "noUser")
                        .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Tài khoản không tồn tại!")));
    }

    @Test @Order(4)
    void login_lockedAccount() throws Exception {
        mockMvc.perform(post("/login/in")
                        .param("username", "abc123")
                        .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Tài khoản đã bị khóa!")));
    }

    @Test @Order(5)
    void login_blankUsername() throws Exception {
        mockMvc.perform(post("/login/in")
                        .param("username", "")
                        .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Tài khoản không tồn tại!")));
    }

    @Test @Order(6)
    void login_blankPassword() throws Exception {
        mockMvc.perform(post("/login/in")
                        .param("username", "admin")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sai mật khẩu!")));
    }

    // ========================== REGISTER (6 TEST) ==========================
    @Test @Order(7)
    void register_success() throws Exception {
        mockMvc.perform(post("/register/in")
                        .param("username", "newuser1")
                        .param("password", "123456")
                        .param("confirm", "123456")
                        .param("email", "newuser1@mail.com"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Đăng ký thành công")));
    }

    @Test @Order(8)
    void register_usernameExists() throws Exception {
        mockMvc.perform(post("/register/in")
                        .param("username", "admin")
                        .param("password", "123456")
                        .param("confirm", "123456")
                        .param("email", "test@mail.com"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Tên đăng nhập đã tồn tại!")));
    }

    @Test @Order(9)
    void register_passwordMismatch() throws Exception {
        mockMvc.perform(post("/register/in")
                        .param("username", "newuser2")
                        .param("password", "123456")
                        .param("confirm", "654321")
                        .param("email", "newuser2@mail.com"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Mật khẩu xác nhận không khớp!")));
    }

    @Test @Order(10)
    void register_invalidEmail() throws Exception {
        mockMvc.perform(post("/register/in")
                        .param("username", "newuser3")
                        .param("password", "123456")
                        .param("confirm", "123456")
                        .param("email", "invalidemail"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Email không hợp lệ")));
    }

    @Test @Order(11)
    void register_blankFields() throws Exception {
        mockMvc.perform(post("/register/in")
                        .param("username", "")
                        .param("password", "")
                        .param("confirm", "")
                        .param("email", ""))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Vui lòng sửa các lỗi bên dưới")));
    }

    @Test @Order(12)
    void register_shortPassword() throws Exception {
        mockMvc.perform(post("/register/in")
                        .param("username", "shortpass")
                        .param("password", "123")
                        .param("confirm", "123")
                        .param("email", "short@mail.com"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Mật khẩu phải có ít nhất")));
    }

    // ========================== GAMEACCOUNT CRUD (8 TEST) ==========================
    @Test @Order(13)
    void listGameAccounts() throws Exception {
        mockMvc.perform(get("/admin/games"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("LOL Account")));
    }

    @Test @Order(14)
    void addGameAccount() throws Exception {
        mockMvc.perform(post("/admin/games/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "New LOL Account")
                        .param("description", "Acc test thêm mới")
                        .param("usernameGame", "lol_new")
                        .param("passwordGame", "pass123")
                        .param("rank", "Vàng IV")
                        .param("price", "300000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/games"));
    }

    @Test @Order(15)
    void editGameAccount() throws Exception {
        GameAccount ga = gameRepo.findAll().get(0);
        mockMvc.perform(get("/admin/games/edit/" + ga.getGameAccId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(ga.getTitle())));
    }

    @Test @Order(16)
    void deleteGameAccount() throws Exception {
        GameAccount ga = new GameAccount();
        ga.setTitle("Delete Me");
        ga.setDescription("Acc test xóa");
        ga.setUsernameGame("delete_test");
        ga.setPasswordGame("123456");
        ga.setRank("Bạc I");
        ga.setPrice(200000.0);
        ga = gameRepo.save(ga);

        mockMvc.perform(get("/admin/games/delete/" + ga.getGameAccId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/games"));
    }

    @Test @Order(17)
    void deleteNonExistGameAccount() throws Exception {
        mockMvc.perform(get("/admin/games/delete/9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/games"));
    }

    @Test @Order(18)
    void addGameAccount_missingTitle() throws Exception {
        mockMvc.perform(post("/admin/games/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "")
                        .param("description", "Thiếu tiêu đề")
                        .param("usernameGame", "acc1")
                        .param("passwordGame", "123456")
                        .param("rank", "Bạc I")
                        .param("price", "200000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/games"));
    }

    @Test @Order(19)
    void updateGameAccount_invalidRank() throws Exception {
        GameAccount ga = gameRepo.findAll().get(0);
        ga.setRank("");
        gameRepo.save(ga);

        mockMvc.perform(get("/admin/games/edit/" + ga.getGameAccId()))
                .andExpect(status().isOk());
    }

    @Test @Order(20)
    void listGameAccounts_pagination() throws Exception {
        mockMvc.perform(get("/admin/games?page=0"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("gamePage")));
    }
}
