package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utilities.Utility;


public class P00_HeaderAndFooter {
    private final By homeLink = By.linkText("Home");
    private final By productsLink = By.cssSelector("a[href='/products']");
    private final By cartLink = By.linkText("Cart");
    private final By signupLoginLink = By.linkText("Signup / Login");
    private final By testCasesLink = By.linkText("Test Cases");
    private final By apiTestingLink = By.linkText("API Testing");
    private final By videoTutorialsLink = By.linkText("Video Tutorials");
    private final By contactUsLink = By.linkText("Contact us");
    private final By logoutLink = By.cssSelector("a[href='/logout']");
    private final By deleteAccountLink = By.cssSelector("a[href='/delete_account']");
    private final By loggedInUserLabel = By.cssSelector("a[href^='/me']");
    private final By loggedInUserText = By.xpath("//a[contains(text(),'Logged in as')]");
    private final By subscriptionTitle = By.xpath("//h2[text()='Subscription']");
    private final By scrollUpArrow = By.id("scrollUp");
    private final By homeBannerText = By.cssSelector("div.item.active div.col-sm-6 h2");
    private final WebDriver driver;

    public P00_HeaderAndFooter(WebDriver driver) {
        this.driver = driver;
    }


    public P05_ProductsPage clickProducts() {
        Utility.clickOnElement(driver, productsLink);
        return new P05_ProductsPage(driver);
    }

    public P06_CartPage clickCart() {
        Utility.clickOnElement(driver, cartLink);
        return new P06_CartPage(driver);
    }

    public P01_Register_LoginPage clickSignupLogin() {
        Utility.clickOnElement(driver, signupLoginLink);
        return new P01_Register_LoginPage(driver);
    }

    public void clickTestCases() {
        Utility.clickOnElement(driver, testCasesLink);
    }


    public P11_ContactUsForm clickContactUs() {
        Utility.clickOnElement(driver, contactUsLink);
        return new P11_ContactUsForm(driver);
    }

    public P01_Register_LoginPage clickLogout() {
        Utility.clickOnElement(driver, logoutLink);
        return new P01_Register_LoginPage(driver);
    }

    public P10_DeleteConfirmation clickDeleteAccount() {
        Utility.clickOnElement(driver, deleteAccountLink);
        return new P10_DeleteConfirmation(driver);
    }


    public boolean isSubscriptionTitleVisible() {
        return Utility.isElementVisible(driver, subscriptionTitle);
    }

    public void clickScrollUpArrow() {
        Utility.clickOnElement(driver, scrollUpArrow);
    }

    public boolean isHomeBannerTextVisible() {
        return Utility.isElementVisible(driver, homeBannerText);
    }


}
