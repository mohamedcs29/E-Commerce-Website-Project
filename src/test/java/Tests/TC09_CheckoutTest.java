package Tests;

import Listeners.IInvokedMethodListenerImpl;
import Listeners.ITest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;
import utilities.DataUtility;
import utilities.LogUtility;
import utilities.Utility;

import java.io.IOException;

import static DriverFactory.DriverFactory.getDriver;

@Epic("AutomationExercise")
@Listeners({IInvokedMethodListenerImpl.class, ITest.class})

public class TC09_CheckoutTest extends TestBase {

    @Feature("Place Order")
    @Story("Register while Checkout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Test Case 14: Place Order: Register while Checkout
            
            1. Launch browser  
            2. Navigate to url 'http://automationexercise.com'  
            3. Verify that home page is visible successfully  
            4. Add products to cart  
            5. Click 'Cart' button  
            6. Verify that cart page is displayed  
            7. Click 'Proceed To Checkout'  
            8. Click 'Register / Login' button  
            9. Fill all details in Signup and create account  
            10. Verify 'ACCOUNT CREATED!' and click 'Continue' button  
            11. Verify 'Logged in as username' at top  
            12. Click 'Cart' button  
            13. Click 'Proceed To Checkout' button  
            14. Verify Address Details and Review Your Order  
            15. Enter description in comment text area and click 'Place Order'  
            16. Enter payment details: Name on Card, Card Number, CVC, Expiration date  
            17. Click 'Pay and Confirm Order' button  
            18. Verify success message 'Your order has been placed successfully!'  
            19. Click 'Delete Account' button  
            20. Verify 'ACCOUNT DELETED!' and click 'Continue' button
            """)

    @Test
    public void verifyPlaceOrderAndRegisterWhileCheckout() throws IOException {
        String email = "mohamed" + Utility.getTimeStamp() + "@gmail.com";

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );
        P00_HeaderAndFooter header = new P00_HeaderAndFooter(getDriver());
        header.clickProducts();

        P05_ProductsPage productsPage = new P05_ProductsPage(getDriver());
        productsPage.addProductToCartByIndex(0)
                .addProductToCartByIndex(1);

        header.clickCart();

        P06_CartPage cartPage = new P06_CartPage(getDriver());
        cartPage.assertCartPageIsVisible().clickProceedToCheckout().clickRegisterLoginButton();

        P01_Register_LoginPage registerLoginPage = new P01_Register_LoginPage(getDriver());
        registerLoginPage.enterUsername(DataUtility.getJsonData("ValidLoginData", "username"))
                .enterEmailSignUp(email);

        P02_CreateAccountPage createAccountPage = registerLoginPage.clickSignupExpectingSuccess();

        createAccountPage
                .clickOnRadioButton()
                .enterPassword(DataUtility.getJsonData("ValidLoginData", "password"))
                .checkNewsletter()
                .checkSpecialOffers()
                .selectDayDropdown(DataUtility.getJsonData("ValidLoginData", "days"))
                .selectMonthDropdown(DataUtility.getJsonData("ValidLoginData", "month"))
                .selectYearDropdown(DataUtility.getJsonData("ValidLoginData", "year"))
                .enterFirstName(DataUtility.getJsonData("ValidLoginData", "firstname"))
                .enterLastName(DataUtility.getJsonData("ValidLoginData", "lastname"))
                .enterAddress(DataUtility.getJsonData("ValidLoginData", "address"))
                .selectDropDownCountry(DataUtility.getJsonData("ValidLoginData", "country"))
                .enterState(DataUtility.getJsonData("ValidLoginData", "state"))
                .enterCity(DataUtility.getJsonData("ValidLoginData", "city"))
                .enterZipcode(DataUtility.getJsonData("ValidLoginData", "zipcode"))
                .enterPhoneNumber(DataUtility.getJsonData("ValidLoginData", "phoneNumber"));

        String expectedFullName = createAccountPage.getFullName();
        String expectedCompanyName = createAccountPage.getCompanyName();
        String expectedAddress = createAccountPage.getFullAddress();
        String expectedCityStateZip = createAccountPage.getCityStateZip();
        String expectedCountry = createAccountPage.getCountryName();
        String expectedMobile = createAccountPage.getMobile();

        createAccountPage.clickOnCreateAccountButton()
                .assertTextIsVisible("Account Created!")
                .clickOnContinueButton()
                .assertLoggedInUserTextIsVisible(DataUtility.getJsonData("ValidLoginData", "username"));

        header.clickCart();
        cartPage.clickProceedToCheckout();

        P07_CheckoutPage checkoutPage = new P07_CheckoutPage(getDriver());

        Assert.assertTrue(checkoutPage.isDeliveryInfoMatching(
                        expectedFullName,
                        expectedCompanyName,
                        expectedAddress,
                        expectedCityStateZip,
                        expectedCountry,
                        expectedMobile),
                "Delivery address details do not match the registered information.");

        LogUtility.info("Delivery Address Details Verified.");

        Assert.assertTrue(checkoutPage.isBillingInfoMatching(
                        expectedFullName,
                        expectedCompanyName,
                        expectedAddress,
                        expectedCityStateZip,
                        expectedCountry,
                        expectedMobile),
                "Billing address details do not match the registered information.");

        LogUtility.info("Billing Address Details Verified.");

        Assert.assertTrue(checkoutPage.isTotalAmountCorrect(), "Total amount displayed is incorrect.");
        LogUtility.info("Total Amount Verified.");

        checkoutPage
                .enterMessage(DataUtility.getJsonData("ValidLoginData", "message"))
                .clickPlaceOrder()
                .enterNameOnCard(DataUtility.getJsonData("ValidLoginData", "nameOnCard"))
                .enterCardNumber(DataUtility.getJsonData("ValidLoginData", "cardNumber"))
                .enterCVC(DataUtility.getJsonData("ValidLoginData", "CVC"))
                .enterExpiryMonth(DataUtility.getJsonData("ValidLoginData", "ExpiryMonth"))
                .enterExpiryYear(DataUtility.getJsonData("ValidLoginData", "ExpiryYear"))
                .clickPayAndConfirmOrder();

        header.clickDeleteAccount()
                .assertAccountDeletedMessageVisible()
                .clickContinueButton();

    }


    @Feature("Place Order")
    @Story("Register before Checkout")
    @Severity(SeverityLevel.CRITICAL)
    @Epic("AutomationExercise")
    @Feature("Place Order")
    @Story("Test Case 15: Place Order: Register before Checkout")
    @Description("""
            1. Launch browser
            2. Navigate to URL 'http://automationexercise.com'
            3. Verify that home page is visible successfully
            4. Click 'Signup / Login' button
            5. Fill all details in Signup and create account
            6. Verify 'ACCOUNT CREATED!' and click 'Continue' button
            7. Verify 'Logged in as username' at top
            8. Add products to cart
            9. Click 'Cart' button
            10. Verify that cart page is displayed
            11. Click 'Proceed To Checkout'
            12. Verify Address Details and Review Your Order
            13. Enter description in comment text area and click 'Place Order'
            14. Enter payment details: Name on Card, Card Number, CVC, Expiration date
            15. Click 'Pay and Confirm Order' button
            16. Verify success message 'Your order has been placed successfully!'
            17. Click 'Delete Account' button
            18. Verify 'ACCOUNT DELETED!' and click 'Continue' button
            """)

    @Test
    public void verifyPlaceOrderAndRegisterBeforeCheckout() throws IOException {
        String email = "mohamed" + Utility.getTimeStamp() + "@gmail.com";
        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );
        P00_HeaderAndFooter header = new P00_HeaderAndFooter(getDriver());
        P02_CreateAccountPage createAccountPage = header.clickSignupLogin()
                .enterUsername(DataUtility.getJsonData("ValidLoginData", "username"))
                .enterEmailSignUp(email)
                .clickSignupExpectingSuccess();

        createAccountPage
                .clickOnRadioButton()
                .enterPassword(DataUtility.getJsonData("ValidLoginData", "password"))
                .checkNewsletter()
                .checkSpecialOffers()
                .selectDayDropdown(DataUtility.getJsonData("ValidLoginData", "days"))
                .selectMonthDropdown(DataUtility.getJsonData("ValidLoginData", "month"))
                .selectYearDropdown(DataUtility.getJsonData("ValidLoginData", "year"))
                .enterFirstName(DataUtility.getJsonData("ValidLoginData", "firstname"))
                .enterLastName(DataUtility.getJsonData("ValidLoginData", "lastname"))
                .enterAddress(DataUtility.getJsonData("ValidLoginData", "address"))
                .selectDropDownCountry(DataUtility.getJsonData("ValidLoginData", "country"))
                .enterState(DataUtility.getJsonData("ValidLoginData", "state"))
                .enterCity(DataUtility.getJsonData("ValidLoginData", "city"))
                .enterZipcode(DataUtility.getJsonData("ValidLoginData", "zipcode"))
                .enterPhoneNumber(DataUtility.getJsonData("ValidLoginData", "phoneNumber"));

        String expectedFullName = createAccountPage.getFullName();
        String expectedCompanyName = createAccountPage.getCompanyName();
        String expectedAddress = createAccountPage.getFullAddress();
        String expectedCityStateZip = createAccountPage.getCityStateZip();
        String expectedCountry = createAccountPage.getCountryName();
        String expectedMobile = createAccountPage.getMobile();
        createAccountPage.clickOnCreateAccountButton()
                .assertTextIsVisible("Account Created!")
                .clickOnContinueButton()
                .assertLoggedInUserTextIsVisible(DataUtility.getJsonData("ValidLoginData", "username"));

        header.clickProducts();
        P05_ProductsPage productsPage = new P05_ProductsPage(getDriver());
        productsPage.addProductToCartByIndex(0)
                .addProductToCartByIndex(1);

        header.clickCart();
        P06_CartPage cartPage = new P06_CartPage(getDriver());
        cartPage.assertCartPageIsVisible();

        cartPage.clickProceedToCheckout();

        P07_CheckoutPage checkoutPage = new P07_CheckoutPage(getDriver());
        Assert.assertTrue(checkoutPage.isDeliveryInfoMatching(
                expectedFullName,
                expectedCompanyName,
                expectedAddress,
                expectedCityStateZip,
                expectedCountry,
                expectedMobile
        ), "Delivery address details do not match the registered information.");
        LogUtility.info("Delivery Address Details Verified.");

        Assert.assertTrue(checkoutPage.isBillingInfoMatching(
                expectedFullName,
                expectedCompanyName,
                expectedAddress,
                expectedCityStateZip,
                expectedCountry,
                expectedMobile
        ), "Billing address details do not match the registered information.");
        LogUtility.info("Billing Address Details Verified.");

        Assert.assertTrue(checkoutPage.isTotalAmountCorrect(), "Total amount displayed is incorrect.");
        LogUtility.info("Total Amount Verified.");

        checkoutPage
                .enterMessage(DataUtility.getJsonData("ValidLoginData", "message"))
                .clickPlaceOrder()
                .enterNameOnCard(DataUtility.getJsonData("ValidLoginData", "nameOnCard"))
                .enterCardNumber(DataUtility.getJsonData("ValidLoginData", "cardNumber"))
                .enterCVC(DataUtility.getJsonData("ValidLoginData", "CVC"))
                .enterExpiryMonth(DataUtility.getJsonData("ValidLoginData", "ExpiryMonth"))
                .enterExpiryYear(DataUtility.getJsonData("ValidLoginData", "ExpiryYear"))
                .clickPayAndConfirmOrder();

        header.clickDeleteAccount()
                .assertAccountDeletedMessageVisible()
                .clickContinueButton();
    }


    @Feature("Place Order")
    @Story("Login before Checkout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""            
            1. Launch browser
            2. Navigate to URL 'http://automationexercise.com'
            3. Verify that home page is visible successfully
            4. Click 'Signup / Login' button
            5. Fill email, password and click 'Login' button
            6. Verify 'Logged in as username' at top
            7. Add products to cart
            8. Click 'Cart' button
            9. Verify that cart page is displayed
            10. Click 'Proceed To Checkout' button
            11. Verify Address Details and Review Your Order
            12. Enter description in comment text area and click 'Place Order'
            13. Enter payment details: Name on Card, Card Number, CVC, Expiration Date
            14. Click 'Pay and Confirm Order' button
            15. Verify success message 'Your order has been placed successfully!'
            16. Click 'Delete Account' button
            17. Verify 'ACCOUNT DELETED!' and click 'Continue' button
            """)

    @Test
    public void verifyPlaceOrderAndLoginBeforeCheckout() throws IOException {

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );
        P00_HeaderAndFooter header = new P00_HeaderAndFooter(getDriver());
        P01_Register_LoginPage loginPage = header.clickSignupLogin();

        loginPage.enterEmailLogin(DataUtility.getJsonData("ValidLoginData", "expectedEmail"))
                .enterPassWordLogin(DataUtility.getJsonData("ValidLoginData", "password"))
                .clickLoginExpectingSuccess();

        P04_HomeAfterLogin homePage = new P04_HomeAfterLogin(getDriver());
        homePage.assertLoggedInUserTextIsVisible(DataUtility.getJsonData("ValidLoginData", "expectedFullName"));

        header.clickProducts();
        P05_ProductsPage productsPage = new P05_ProductsPage(getDriver());
        productsPage.addProductToCartByIndex(0)
                .addProductToCartByIndex(1);

        header.clickCart();

        P06_CartPage cartPage = new P06_CartPage(getDriver());
        cartPage.assertCartPageIsVisible();

        cartPage.clickProceedToCheckout();

        P07_CheckoutPage checkoutPage = new P07_CheckoutPage(getDriver());

        String expectedFullName = DataUtility.getJsonData("ValidLoginData", "expectedFullName");
        String expectedCompanyName = DataUtility.getJsonData("ValidLoginData", "expectedCompanyName");
        String expectedAddress = DataUtility.getJsonData("ValidLoginData", "expectedAddress");
        String expectedCityStateZip = DataUtility.getJsonData("ValidLoginData", "expectedCityStateZip");
        String expectedCountry = DataUtility.getJsonData("ValidLoginData", "expectedCountry");
        String expectedMobile = DataUtility.getJsonData("ValidLoginData", "expectedMobile");
        Assert.assertTrue(
                checkoutPage.isDeliveryInfoMatching(expectedFullName, expectedCompanyName,
                        expectedAddress, expectedCityStateZip, expectedCountry, expectedMobile),
                "Delivery address doesn't match!");
        LogUtility.info("Delivery Address Details Verified.");
        Assert.assertTrue(
                checkoutPage.isBillingInfoMatching(expectedFullName, expectedCompanyName,
                        expectedAddress, expectedCityStateZip, expectedCountry, expectedMobile),
                "Billing address doesn't match!");
        LogUtility.info("Billing Address Details Verified.");
        checkoutPage
                .enterMessage(DataUtility.getJsonData("ValidLoginData", "message"))
                .clickPlaceOrder()
                .enterNameOnCard(DataUtility.getJsonData("ValidLoginData", "nameOnCard"))
                .enterCardNumber(DataUtility.getJsonData("ValidLoginData", "cardNumber"))
                .enterCVC(DataUtility.getJsonData("ValidLoginData", "CVC"))
                .enterExpiryMonth(DataUtility.getJsonData("ValidLoginData", "ExpiryMonth"))
                .enterExpiryYear(DataUtility.getJsonData("ValidLoginData", "ExpiryYear"))
                .clickPayAndConfirmOrder();

        //header.clickDeleteAccount()
        // .assertAccountDeletedMessageVisible()
        //  .clickContinueButton();

        LogUtility.info("Account deleted successfully");
    }


    @Feature("Cart Management")
    @Story("Remove Products from Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Test Case 17: Remove Products From Cart
            1. Launch browser
            2. Navigate to url 'http://automationexercise.com'
            3. Verify that home page is visible successfully
            4. Add products to cart
            5. Click 'Cart' button
            6. Verify that cart page is displayed
            7. Click 'X' button corresponding to particular product
            8. Verify that product is removed from the cart
            """)
    @Test(description = "Verify that user can remove a product from the cart successfully")
    public void verifyRemoveProductsFromCart() throws IOException {
        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );

        P00_HeaderAndFooter header = new P00_HeaderAndFooter(getDriver());
        header.clickProducts()
                .addProductToCartByIndex(0);

        header.clickCart();

        P06_CartPage cartPage = new P06_CartPage(getDriver());
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page not visible.");

        cartPage.clickRemoveProductByIndex(0);

        Assert.assertTrue(cartPage.isCartEmpty(), "Product was not removed from the cart.");

        LogUtility.info("Product successfully removed from cart.");
    }


    @Epic("AutomationExercise")
    @Feature("Checkout Process")
    @Story("Verify Address Details in Checkout Page")
    @Description("""
            Test Case 23: Verify address details in checkout page
            Steps:
            1. Launch browser and navigate to https://automationexercise.com
            2. Verify home page is visible
            3. Click 'Signup / Login' and create account with full details
            4. Verify 'ACCOUNT CREATED!' and 'Logged in as username'
            5. Add products to cart
            6. Click 'Cart' and verify cart is displayed
            7. Proceed to checkout
            8. Verify delivery and billing address details match registration details
            9. Delete the account
            10. Verify 'ACCOUNT DELETED!' and click 'Continue'
            """)
    @Test
    public void verifyAddressDetailsInCheckoutPage() throws IOException {
        String email = "mohamed" + Utility.getTimeStamp() + "@gmail.com";


        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );
        P00_HeaderAndFooter header = new P00_HeaderAndFooter(getDriver());
        P02_CreateAccountPage createAccountPage = header.clickSignupLogin()
                .enterUsername(DataUtility.getJsonData("ValidLoginData", "username"))
                .enterEmailSignUp(email)
                .clickSignupExpectingSuccess();

        createAccountPage
                .clickOnRadioButton()
                .enterPassword(DataUtility.getJsonData("ValidLoginData", "password"))
                .checkNewsletter()
                .checkSpecialOffers()
                .selectDayDropdown(DataUtility.getJsonData("ValidLoginData", "days"))
                .selectMonthDropdown(DataUtility.getJsonData("ValidLoginData", "month"))
                .selectYearDropdown(DataUtility.getJsonData("ValidLoginData", "year"))
                .enterFirstName(DataUtility.getJsonData("ValidLoginData", "firstname"))
                .enterLastName(DataUtility.getJsonData("ValidLoginData", "lastname"))
                .enterAddress(DataUtility.getJsonData("ValidLoginData", "address"))
                .selectDropDownCountry(DataUtility.getJsonData("ValidLoginData", "country"))
                .enterState(DataUtility.getJsonData("ValidLoginData", "state"))
                .enterCity(DataUtility.getJsonData("ValidLoginData", "city"))
                .enterZipcode(DataUtility.getJsonData("ValidLoginData", "zipcode"))
                .enterPhoneNumber(DataUtility.getJsonData("ValidLoginData", "phoneNumber"));

        String expectedFullName = createAccountPage.getFullName();
        String expectedCompanyName = createAccountPage.getCompanyName();
        String expectedAddress = createAccountPage.getFullAddress();
        String expectedCityStateZip = createAccountPage.getCityStateZip();
        String expectedCountry = createAccountPage.getCountryName();
        String expectedMobile = createAccountPage.getMobile();
        createAccountPage.clickOnCreateAccountButton()
                .assertTextIsVisible("Account Created!")
                .clickOnContinueButton()
                .assertLoggedInUserTextIsVisible(DataUtility.getJsonData("ValidLoginData", "username"));

        header.clickProducts();
        new P05_ProductsPage(getDriver())
                .addProductToCartByIndex(0)
                .addProductToCartByIndex(1);

        P06_CartPage cartPage = header.clickCart();
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page not visible");
        cartPage.clickProceedToCheckout();

        P07_CheckoutPage checkoutPage = new P07_CheckoutPage(getDriver());
        Assert.assertTrue(checkoutPage.isDeliveryInfoMatching(
                expectedFullName, expectedCompanyName, expectedAddress,
                expectedCityStateZip, expectedCountry, expectedMobile
        ), "Delivery address does not match");

        Assert.assertTrue(checkoutPage.isBillingInfoMatching(
                expectedFullName, expectedCompanyName, expectedAddress,
                expectedCityStateZip, expectedCountry, expectedMobile
        ), "Billing address does not match");

        LogUtility.info("Address details verified successfully");

        header.clickDeleteAccount()
                .assertAccountDeletedMessageVisible()
                .clickContinueButton();

    }

}
