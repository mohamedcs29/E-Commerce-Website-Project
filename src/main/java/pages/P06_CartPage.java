package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utilities.Utility;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static DriverFactory.DriverFactory.getDriver;

public class P06_CartPage {
    private final WebDriver driver;

    private final By cartProductNames = By.xpath("//tr/td[@class='cart_description']/h4/a");
    private final By cartQuantities = By.xpath("//tr/td[@class='cart_quantity']/button");
    private final By cartPrices = By.xpath("//tr/td[@class='cart_price']/p");
    private final By cartTotals = By.xpath("//tr/td[@class='cart_total']/p");
    private final By subscriptionTitle = By.xpath("//h2[normalize-space()='Subscription']");
    private final By emailInput = By.id("susbscribe_email");
    private final By submitButton = By.id("subscribe");  // الزرار اللي عليه السهم
    private final By ProceedToCheckoutButton = By.cssSelector("a[class='btn btn-default check_out']");
    private final By cartItems = By.cssSelector("tr[id^='product-']");
    private final By removeButtons = By.cssSelector("a.cart_quantity_delete"); // زرار X لحذف المنتج
    By registerLoginFromModal = By.cssSelector("div.modal-body a[href='/login']");

    public P06_CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<String> getProductNamesInCart() {
        List<WebElement> elements = driver.findElements(cartProductNames);
        List<String> names = new ArrayList<>();
        for (WebElement e : elements) {
            names.add(e.getText().trim());
        }
        return names;
    }

    public List<Integer> getQuantitiesInCart() {
        List<WebElement> elements = driver.findElements(cartQuantities);
        List<Integer> quantities = new ArrayList<>();
        for (WebElement e : elements) {
            quantities.add(Integer.parseInt(e.getText().trim()));
        }
        return quantities;
    }

    public List<Double> getPricesInCart() {
        List<WebElement> elements = driver.findElements(cartPrices);
        List<Double> prices = new ArrayList<>();
        for (WebElement e : elements) {
            String cleaned = e.getText().replaceAll("[^0-9]", "");
            prices.add(Double.parseDouble(cleaned));
        }
        return prices;
    }

    public List<Double> getTotalsInCart() {
        List<WebElement> elements = driver.findElements(cartTotals);
        List<Double> totals = new ArrayList<>();
        for (WebElement e : elements) {
            String cleaned = e.getText().replaceAll("[^0-9]", "");
            totals.add(Double.parseDouble(cleaned));
        }
        return totals;
    }

    public boolean areProductQuantitiesCorrect(Map<String, Integer> expectedCounts) {
        List<String> names = getProductNamesInCart();
        List<Integer> quantities = getQuantitiesInCart();

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            int actual = quantities.get(i);
            int expected = expectedCounts.getOrDefault(name, -1);

            if (actual != expected) {
                System.out.println("Quantity mismatch for '" + name + "': expected " + expected + ", but found " + actual);
                return false;
            }
        }
        return true;
    }

    public boolean isEachProductTotalCorrect() {
        List<Double> prices = getPricesInCart();
        List<Integer> quantities = getQuantitiesInCart();
        List<Double> totals = getTotalsInCart();

        for (int i = 0; i < prices.size(); i++) {
            double expectedTotal = prices.get(i) * quantities.get(i);
            double actualTotal = totals.get(i);

            if (Math.abs(expectedTotal - actualTotal) > 0.00) {
                System.out.println("Total mismatch at row " + (i + 1)
                        + " | price: " + prices.get(i)
                        + " × qty: " + quantities.get(i)
                        + " = " + expectedTotal
                        + " but found: " + actualTotal);
                return false;
            }
        }
        return true;
    }

    public P06_CartPage scrollToSubscriptionSection() {
        Utility.scrolling(driver, subscriptionTitle);
        return this;

    }

    public boolean isSubscriptionTextVisible() {
        return driver.findElement(subscriptionTitle).isDisplayed();
    }

    public P06_CartPage subscribeWithEmail(String email) {
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


    public P06_CartPage clickProceedToCheckout() {
        Utility.clickOnElement(driver, ProceedToCheckoutButton);
        return this;
    }

    public P02_CreateAccountPage clickRegisterLoginButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.modal-content")));

        wait.until(ExpectedConditions.elementToBeClickable(registerLoginFromModal));

        Utility.clickOnElement(driver, registerLoginFromModal);

        return new P02_CreateAccountPage(driver);
    }


    public void clickRemoveProductByIndex(int index) {
        List<WebElement> removeBtns = driver.findElements(removeButtons);
        if (removeBtns.size() > index) {
            removeBtns.get(index).click();
        } else {
            throw new IllegalArgumentException("No remove button at index: " + index);
        }

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.invisibilityOfElementLocated(cartItems));
    }

    public boolean isCartEmpty() {
        return driver.findElements(cartItems).isEmpty();
    }

    public boolean isCartPageVisible() {
        return Utility.verifyText(driver, By.xpath("//li[contains(text(),'Shopping Cart')]"), "Shopping Cart");
    }

    public boolean areCartItemsVisible() {
        By cartItems = By.cssSelector(".cart_info tbody tr");
        return !driver.findElements(cartItems).isEmpty();
    }


    public boolean verifyProductQuantityInCart(String expectedQuantity) {
        By quantityLocator = By.cssSelector("td.cart_quantity > button.disabled");


        if (driver.findElements(quantityLocator).isEmpty()) {
            return false;
        }


        String actualQuantity = driver.findElement(quantityLocator).getText().trim();

        return actualQuantity.equals(expectedQuantity);
    }

    public P06_CartPage assertCartPageIsVisible() {
        Assert.assertTrue(Utility.isCurrentUrlEquals(getDriver(), "https://automationexercise.com/view_cart"));
        return this;

    }


}
