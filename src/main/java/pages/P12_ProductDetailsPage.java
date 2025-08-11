package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Utility;

import java.time.Duration;

public class P12_ProductDetailsPage {

    private final WebDriver driver;

    // === Locators ===
    private final By productName = By.cssSelector(".product-information h2");
    private final By category = By.xpath("//p[contains(text(),'Category')]");
    private final By price = By.cssSelector("span span");
    private final By availability = By.xpath("//b[contains(text(),'Availability:')]/parent::p");
    private final By condition = By.xpath("//b[contains(text(),'Condition:')]/parent::p");
    private final By brand = By.xpath("//b[contains(text(),'Brand:')]/parent::p");
    private final By reviewSectionTitle = By.xpath("//a[text()='Write Your Review']");
    private final By nameInput = By.id("name");
    private final By emailInput = By.id("email");
    private final By reviewInput = By.id("review");
    private final By submitButton = By.id("button-review");
    private final By successMessage = By.xpath("//span[contains(text(),'Thank you for your review.')]");
    private final By addToCartButton = By.cssSelector("button[class='btn btn-default cart']");

    public P12_ProductDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isProductDetailsVisible() {
        return Utility.isElementVisible(driver, productName) &&
                Utility.isElementVisible(driver, category) &&
                Utility.isElementVisible(driver, price) &&
                Utility.isElementVisible(driver, availability) &&
                Utility.isElementVisible(driver, condition) &&
                Utility.isElementVisible(driver, brand);
    }

    public P12_ProductDetailsPage setQuantity(String quantity) {
        WebElement quantityInput = driver.findElement(By.id("quantity"));
        quantityInput.clear();
        quantityInput.sendKeys(quantity);
        return this;
    }

    public P12_ProductDetailsPage clickAddToCart() {
        driver.findElement(addToCartButton).click();


        By modalLocator = By.id("cartModal");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(modalLocator));
        return this;
    }

    public P06_CartPage clickViewCartFromModal() {
        By viewCartLinkLocator = By.cssSelector("div.modal-body a[href='/view_cart']");

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(viewCartLinkLocator))
                .click();

        return new P06_CartPage(driver);
    }

    public boolean isReviewSectionVisible() {
        return driver.findElement(reviewSectionTitle).isDisplayed();
    }

    public P12_ProductDetailsPage enterReviewName(String name) {
        Utility.sendData(driver, nameInput, name);
        return this;
    }

    public P12_ProductDetailsPage enterReviewEmail(String email) {
        Utility.sendData(driver, emailInput, email);
        return this;
    }

    public P12_ProductDetailsPage enterReviewText(String reviewText) {
        Utility.sendData(driver, reviewInput, reviewText);
        return this;
    }

    public void submitReview() {
        Utility.clickOnElement(driver, submitButton);
    }

    public boolean isReviewSuccessMessageVisible() {
        return driver.findElement(successMessage).isDisplayed();
    }
}

