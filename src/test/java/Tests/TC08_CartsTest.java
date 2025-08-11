package Tests;

import Listeners.IInvokedMethodListenerImpl;
import Listeners.ITest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.P00_HeaderAndFooter;
import pages.P06_CartPage;
import pages.P12_ProductDetailsPage;
import utilities.DataUtility;
import utilities.Utility;

import java.io.IOException;

import static DriverFactory.DriverFactory.getDriver;

@Listeners({IInvokedMethodListenerImpl.class, ITest.class})

public class TC08_CartsTest extends TestBase {

    @Feature("Cart Page")
    @Story("Verify Subscription in Footer")
    @Severity(SeverityLevel.MINOR)
    @Description("""
                Steps:
                1. Launch browser
                2. Navigate to url 'http://automationexercise.com'
                3. Verify that home page is visible successfully
                4. Click 'Cart' button
                5. Scroll down to footer
                6. Verify text 'SUBSCRIPTION'
                7. Enter email address in input and click arrow button
                8. Verify success message 'You have been successfully subscribed!' is visible
            """)
    @Test
    public void verifySubscriptionInCartPage() throws IOException {

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );

        new P00_HeaderAndFooter(getDriver())
                .clickCart()
                .scrollToSubscriptionSection()
                .subscribeWithEmail(DataUtility.getJsonData("ValidLoginData", "registeredEmail"));

        Assert.assertTrue(
                new P06_CartPage(getDriver()).isSubscriptionTextVisible(),
                "'SUBSCRIPTION' text is not visible on Cart page."
        );

        Assert.assertTrue(
                new P06_CartPage(getDriver()).isSubscriptionSuccessVisible(),
                "Success message for subscription is not visible on Cart page."
        );
    }


    @Epic("AutomationExercise")
    @Feature("Cart Functionality")
    @Story("Verify Product Quantity in Cart")
    @Description("""
                Steps:
                1. Launch browser
                2. Navigate to url 'http://automationexercise.com'
                3. Verify that home page is visible successfully
                4. Click 'View Product' for any product on home page
                5. Verify product detail is opened
                6. Increase quantity to 4
                7. Click 'Add to cart' button
                8. Click 'View Cart' button
                9. Verify that product is displayed in cart page with exact quantity
            """)
    @Test
    public void verifyProductQuantityInCart() throws IOException {

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );

        P00_HeaderAndFooter homePage = new P00_HeaderAndFooter(getDriver());
        P12_ProductDetailsPage productDetails = homePage
                .clickProducts()
                .clickViewProductByIndex(0);

        Assert.assertTrue(
                productDetails.isProductDetailsVisible(),
                "Product details not visible"
        );

        productDetails.setQuantity(DataUtility.getJsonData("ValidLoginData", "quantityOfProduct"));
        productDetails.clickAddToCart();

        P06_CartPage cartPage = productDetails.clickViewCartFromModal();

        Assert.assertTrue(
                cartPage.verifyProductQuantityInCart(DataUtility.getJsonData("ValidLoginData", "quantityOfProduct")),
                "Product quantity is not correct in cart"
        );

    }

}



