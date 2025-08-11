package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utilities.LogUtility;
import utilities.Utility;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class P05_ProductsPage {
    private final WebDriver driver;
    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By searchResultsTitle = By.xpath("//h2[contains(text(),'Searched Products')]");
    private final By productNames = By.xpath("//div[@class='productinfo text-center']/p");

    private final By productContainer = By.cssSelector("div.features_items .product-image-wrapper");
    private final By continueShoppingButton = By.cssSelector("button[data-dismiss='modal']");
    private final By allProductsTitle = By.cssSelector(".title.text-center");
    private final By productsList = By.cssSelector(".features_items");

    private final Map<String, Integer> addedProductCounts = new HashMap<>();

    public P05_ProductsPage(WebDriver driver) {
        this.driver = driver;
    }


    public P05_ProductsPage assertTextProductsIsVisible(String expectedText) {
        Assert.assertTrue(
                Utility.verifyText(driver, allProductsTitle, expectedText),
                "Text mismatch! Expected: '" + expectedText + "'"
        );
        return this;
    }

    public P05_ProductsPage productsListIsVisible() {
        LogUtility.info("Verifying that product list is visible...");
        Assert.assertTrue(driver.findElement(productsList).isDisplayed(), "Product list is not visible.");
        LogUtility.info("Product list is visible.");
        Utility.giveWait(5);
        return this;
    }


    public P05_ProductsPage searchForProduct(String keyword) {
        Utility.sendData(driver, searchInput, keyword);
        Utility.clickOnElement(driver, searchButton);
        return this;
    }

    public boolean isSearchResultsEmpty() {
        return driver.findElements(By.cssSelector(".product-image-wrapper")).isEmpty();
    }


    public boolean isSearchResultsDisplayed() {
        return driver.findElement(searchResultsTitle).isDisplayed();
    }

    public boolean areAllResultsMatching(String keyword) {
        List<String> productTexts = driver.findElements(productNames).stream()
                .map(e -> e.getText().toLowerCase())
                .collect(Collectors.toList());

        if (productTexts.isEmpty()) {
            System.out.println("No search results found.");
            return false;
        }

        return productTexts.stream().allMatch(name -> name.contains(keyword.toLowerCase()));
    }

    public P05_ProductsPage addProductToCartByIndex(int index) {

        By specificContainer = By.xpath("(//div[@class='features_items']//div[@class='product-image-wrapper'])[" + (index + 1) + "]");
        Utility.scrolling(driver, specificContainer);

        String productName = driver.findElements(productContainer)
                .get(index)
                .findElement(By.tagName("p"))
                .getText()
                .trim();

        addedProductCounts.put(productName, addedProductCounts.getOrDefault(productName, 0) + 1);

        new Actions(driver)
                .moveToElement(driver.findElements(productContainer).get(index))
                .moveToElement(driver.findElements(productContainer).get(index)
                        .findElement(By.xpath(".//a[contains(text(),'Add to cart')]")))
                .click()
                .perform();

        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.elementToBeClickable(continueShoppingButton))
                .click();

        return this;
    }


    public Map<String, Integer> getAddedProductCounts() {
        return addedProductCounts;
    }


    public P05_ProductsPage addAllProductsToCart() {
        String baseAddToCartXpath = "(//div[@class='productinfo text-center']/a[contains(text(),'Add to cart')])";
        By continueShoppingSelector = By.cssSelector("button[data-dismiss='modal']");
        int productCount = driver.findElements(By.xpath(baseAddToCartXpath)).size();
        for (int i = 1; i <= productCount; i++) {

            By currentProductLocator = By.xpath(baseAddToCartXpath + "[" + i + "]");
            Utility.scrolling(driver, currentProductLocator);
            Utility.clickOnElement(driver, currentProductLocator);
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(continueShoppingSelector))
                    .click();
        }
        return this;
    }

    public P12_ProductDetailsPage clickViewProductByIndex(int index) {
        By viewProductButton = By.xpath("(//a[contains(text(),'View Product')])[" + (index + 1) + "]");
        Utility.clickOnElement(driver, viewProductButton);
        return new P12_ProductDetailsPage(driver);
    }


}
