package com.example.asm_java5.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class GameAccountTest extends BaseTest {

    @BeforeClass
    public void loginAdmin() {
        driver.get("http://localhost:8080/login");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.urlContains("/home"));
    }

    @Test(priority = 1)
    public void listGames_success() {
        driver.get("http://localhost:8080/admin/games");
        Assert.assertTrue(driver.getTitle().contains("Game"));
    }

    @Test(priority = 2)
    public void createGame_success() {
        driver.get("http://localhost:8080/admin/games");
        driver.findElement(By.name("name")).sendKeys("PUBG Account");
        driver.findElement(By.name("price")).sendKeys("150000");
        driver.findElement(By.name("description")).sendKeys("Nick PUBG VIP");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Assert.assertTrue(driver.getPageSource().contains("PUBG Account"));
    }

    @Test(priority = 3)
    public void createGame_missingName() {
        driver.get("http://localhost:8080/admin/games");
        driver.findElement(By.name("price")).sendKeys("150000");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Assert.assertTrue(driver.getPageSource().contains("Tên game không được để trống"));
    }

    @Test(priority = 4)
    public void editGame_success() {
        driver.get("http://localhost:8080/admin/games/edit/1");
        driver.findElement(By.name("description")).clear();
        driver.findElement(By.name("description")).sendKeys("Đã chỉnh sửa");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Assert.assertTrue(driver.getPageSource().contains("Đã chỉnh sửa"));
    }

    @Test(priority = 5)
    public void deleteGame_success() {
        driver.get("http://localhost:8080/admin/games/delete/1");
        Assert.assertTrue(driver.getCurrentUrl().contains("/admin/games"));
    }

    @Test(priority = 6)
    public void pagination_nextPage() {
        driver.get("http://localhost:8080/admin/games?page=1");
        Assert.assertTrue(driver.getPageSource().contains("Trang 2"));
    }

    @Test(priority = 7)
    public void editGame_notFound() {
        driver.get("http://localhost:8080/admin/games/edit/999");
        Assert.assertTrue(driver.getPageSource().contains("Không tìm thấy"));
    }

    @Test(priority = 8)
    public void deleteGame_notFound() {
        driver.get("http://localhost:8080/admin/games/delete/999");
        Assert.assertTrue(driver.getPageSource().contains("Không tồn tại"));
    }
}
