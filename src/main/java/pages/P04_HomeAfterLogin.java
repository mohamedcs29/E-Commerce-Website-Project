package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utilities.LogUtility;
import utilities.Utility;

import java.time.Duration;


public class P04_HomeAfterLogin {
    private final WebDriver driver;
    private final By firstProductContainer = By.xpath("(//div[@class='features_items']//div[@class='product-image-wrapper'])[1]");
    private final By addToCartButtonFirstProduct = By.xpath("(//div[@class='features_items']//div[@class='product-image-wrapper'])[1]//a[contains(text(),'Add to cart')]");
    private final By continueShoppingButton = By.cssSelector("button[data-dismiss='modal']");
    private final By loggedInUserText = By.cssSelector("a b");
    private final By subscriptionTitle = By.xpath("//h2[normalize-space()='Subscription']");
    private final By emailInput = By.id("susbscribe_email");
    private final By submitButton = By.id("subscribe");  // الزرار اللي عليه السهم
    private final By successMessage = By.xpath("//div[@class='alert-success' and contains(text(),'successfully subscribed')]");
    private final By recommendedSection = By.xpath("//h2[text()='recommended items']");
    private final By firstRecommendedAddToCart = By.cssSelector("div.recommended_items a.btn.btn-default.add-to-cart[data-product-id='4']:nth-of-type(1)");
    private final By viewCartButton = By.xpath("//u[text()='View Cart']");

    public P04_HomeAfterLogin(WebDriver driver) {

        this.driver = driver;
    }


    public P00_HeaderAndFooter assertLoggedInUserTextIsVisible(String expectedText) {
        LogUtility.info("Checking if logged-in text is visible and correct: '" + expectedText + "'");

        Assert.assertTrue(
                Utility.verifyText(driver, loggedInUserText, expectedText),
                "Text mismatch! Expected: '" + expectedText + "'"
        );
        return new P00_HeaderAndFooter(driver);
    }

    public P04_HomeAfterLogin scrollToSubscriptionSection() {
        Utility.scrolling(driver, subscriptionTitle);
        return this;

    }

    public boolean isSubscriptionTextVisible() {
        return driver.findElement(subscriptionTitle).isDisplayed();
    }

    public P04_HomeAfterLogin subscribeWithEmail(String email) {
        Utility.sendData(driver, emailInput, email);
        Utility.clickOnElement(driver, submitButton);
        return this;
    }

    public boolean isSubscriptionSuccessVisible() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//*[contains(text(),'You have been successfully subscribed!')]")
                    ))
                    .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isRecommendedSectionVisible() {
        Utility.scrolling(driver, recommendedSection);  // Scroll to section
        return driver.findElement(recommendedSection).isDisplayed();
    }

    public void addFirstRecommendedItemToCart() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(firstRecommendedAddToCart))
                .click();
    }

    public void clickViewCartButton() {
        By viewCart = By.xpath("//u[text()='View Cart']");
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.elementToBeClickable(viewCart));
        driver.findElement(viewCart).click();
    }


}