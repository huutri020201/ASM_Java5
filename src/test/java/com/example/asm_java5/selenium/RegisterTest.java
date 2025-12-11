package com.example.asm_java5.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class RegisterTest extends BaseTest {

    @Test(priority = 1)
    public void register_success() {
        driver.get("http://localhost:8080/register");
        driver.findElement(By.name("username")).sendKeys("user01");
        driver.findElement(By.name("password")).sendKeys("123");
        driver.findElement(By.name("confirm")).sendKeys("123");
        driver.findElement(By.name("fullname")).sendKeys("Nguyen Van A");
        driver.findElement(By.name("email")).sendKeys("user01@gmail.com");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String message = driver.findElement(By.className("alert")).getText();
        Assert.assertTrue(message.contains("Đăng ký thành công"));
    }

    @Test(priority = 2)
    public void register_passwordMismatch() {
        driver.get("http://localhost:8080/register");
        driver.findElement(By.name("username")).sendKeys("user02");
        driver.findElement(By.name("password")).sendKeys("123");
        driver.findElement(By.name("confirm")).sendKeys("456");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String message = driver.findElement(By.className("alert")).getText();
        Assert.assertTrue(message.contains("Mật khẩu xác nhận không khớp"));
    }

    @Test(priority = 3)
    public void register_usernameExists() {
        driver.get("http://localhost:8080/register");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("123");
        driver.findElement(By.name("confirm")).sendKeys("123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String message = driver.findElement(By.className("alert")).getText();
        Assert.assertTrue(message.contains("Tên đăng nhập đã tồn tại"));
    }

    @Test(priority = 4)
    public void register_emptyFields() {
        driver.get("http://localhost:8080/register");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        String message = driver.findElement(By.className("alert")).getText();
        Assert.assertTrue(message.contains("Vui lòng sửa các lỗi"));
    }

    @Test(priority = 5)
    public void register_invalidEmail() {
        driver.get("http://localhost:8080/register");
        driver.findElement(By.name("username")).sendKeys("user05");
        driver.findElement(By.name("password")).sendKeys("123");
        driver.findElement(By.name("confirm")).sendKeys("123");
        driver.findElement(By.name("email")).sendKeys("invalid-email");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String message = driver.findElement(By.className("alert")).getText();
        Assert.assertTrue(message.contains("Email không hợp lệ"));
    }

    @Test(priority = 6)
    public void register_shortPassword() {
        driver.get("http://localhost:8080/register");
        driver.findElement(By.name("username")).sendKeys("user06");
        driver.findElement(By.name("password")).sendKeys("12");
        driver.findElement(By.name("confirm")).sendKeys("12");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String message = driver.findElement(By.className("alert")).getText();
        Assert.assertTrue(message.contains("Mật khẩu quá ngắn"));
    }
}
