package com.periplus.tests;

import io.github.cdimascio.dotenv.Dotenv;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.Assert;
import org.testng.annotations.*;

public class ShoppingCartTest {

    private WebDriver driver;
    private WebDriverWait wait;
    
    private static final Dotenv dotenv = Dotenv.load();

    private final String baseUrl = "https://www.periplus.com/";
    private final String testEmail = dotenv.get("PERIPLUS_EMAIL");
    private final String testPassword = dotenv.get("PERIPLUS_PASSWORD");
    private final String testISBN = "9781568363912";

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testLoginAndAddProductToCart() {
        // Navigate to the website
        driver.get(baseUrl);

        // Login
        click(By.linkText("Sign In"));
        type(By.name("email"), testEmail);
        type(By.name("password"), testPassword);
        click(By.id("button-login"));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("button-login")));

        // Verify login success
        List<WebElement> warningMessages = driver.findElements(By.cssSelector("div.warning"));
        if (!warningMessages.isEmpty()) {
            Assert.fail("Login failed: " + warningMessages.get(0).getText());
        }

        WebElement signInText = driver.findElement(By.id("nav-signin-text"));
        Assert.assertNotEquals(signInText.getText().trim(), "Sign In", "Login failed: 'Sign In' text is still present.");

        // Find product
        wait.until(ExpectedConditions.elementToBeClickable(By.id("filter_name")));
        type(By.id("filter_name"), testISBN);
        click(By.cssSelector("button[type='submit']"));

        // Add one product to the cart
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button.btn.btn-add-to-cart")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".preloader")));
        addToCartButton.click();

        // Verify modal success
        WebElement successModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.modal-text")));
        Assert.assertTrue(successModal.getText().toLowerCase().contains("success add to cart"),
                "Product was not successfully added to cart.");

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("Notification-Modal")));

        // Go to cart
        click(By.cssSelector("#show-your-cart a.single-icon"));
        
        // Verify that the product has been successfully added to the cart
        WebElement isbnInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'row-cart-product')]//*[contains(text(), '" + testISBN + "')]")));
        Assert.assertTrue(isbnInCart.isDisplayed(), "Product with ISBN " + testISBN + " not found in cart.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // -----------------
    // Helper Methods
    // -----------------

    private void click(By selector) {
        wait.until(ExpectedConditions.elementToBeClickable(selector)).click();
    }

    private void type(By selector, String text) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(selector));
        element.clear();
        element.sendKeys(text);
    }
}
