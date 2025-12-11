package com.example.asm_java5.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest extends BaseTest {

    public void safeClick(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    @Test(priority = 1)
    public void login_success() {
        driver.get("http://localhost:8080/login");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("123456");
        safeClick(By.cssSelector("button[type='submit']"));

        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.urlContains("/home"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/home"));
    }

    @Test(priority = 2)
    public void login_wrongPassword() {
        driver.get("http://localhost:8080/login");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("wrong");
        safeClick(By.cssSelector("button[type='submit']"));

        String message = driver.findElement(By.className("alert")).getText();
        Assert.assertTrue(message.contains("Sai mật khẩu"));
    }

    @Test(priority = 3)
    public void login_nonExistAccount() {
        driver.get("http://localhost:8080/login");
        driver.findElement(By.name("username")).sendKeys("ghost");
        driver.findElement(By.name("password")).sendKeys("123");
        safeClick(By.cssSelector("button[type='submit']"));


        String message = driver.findElement(By.className("alert")).getText();
        Assert.assertTrue(message.contains("Tài khoản không tồn tại"));
    }

    @Test(priority = 4)
    public void login_lockedAccount() {
        driver.get("http://localhost:8080/login");
        driver.findElement(By.name("username")).sendKeys("abc123");
        driver.findElement(By.name("password")).sendKeys("123456");
        safeClick(By.cssSelector("button[type='submit']"));

        String message = driver.findElement(By.className("alert")).getText();
        Assert.assertTrue(message.contains("Tài khoản đã bị khóa"));
    }

    @Test(priority = 5)
    public void login_emptyField() {
        driver.get("http://localhost:8080/login");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String message = driver.findElement(By.className("alert")).getText();
        Assert.assertTrue(message.contains("Vui lòng nhập"));
    }

    @Test(priority = 6)
    public void logout_success() {
        driver.get("http://localhost:8080/logout");
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.urlContains("/login"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"));
    }
}
