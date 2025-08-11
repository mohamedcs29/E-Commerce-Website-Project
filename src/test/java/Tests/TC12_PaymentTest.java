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

@Listeners({IInvokedMethodListenerImpl.class, ITest.class})

public class TC12_PaymentTest extends TestBase {

    @Epic("AutomationExercise")
    @Feature("Order Process")
    @Severity(SeverityLevel.NORMAL)
    @Story("Download Invoice After Successful Purchase")
    @Description("""
            Test Case 24: Download Invoice After Purchase
            Steps:
            1. Launch browser and navigate to 'https://automationexercise.com'
            2. Verify home page is visible
            3. Add products to cart
            4. Click 'Cart' and verify cart is displayed
            5. Proceed to checkout
            6. Click 'Register / Login' and create new account
            7. Verify 'ACCOUNT CREATED!' and 'Logged in as username'
            8. Proceed again to checkout
            9. Verify address details and review order
            10. Enter comment and place order
            11. Enter payment details and confirm order
            12. Verify success message
            13. Download invoice and verify it's saved
            14. Click 'Continue'
            15. Delete account and verify 'ACCOUNT DELETED!'
            """)
    @Test
    public void verifyDownloadInvoiceAfterPurchase() throws IOException {
        String email = "mohamed" + Utility.getTimeStamp() + "@gmail.com";

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );
        P00_HeaderAndFooter header = new P00_HeaderAndFooter(getDriver());
        header.clickProducts();

        P05_ProductsPage productsPage = new P05_ProductsPage(getDriver());
        productsPage.addProductToCartByIndex(0); // Add the first product

        header.clickCart();

        P06_CartPage cartPage = new P06_CartPage(getDriver());
        Assert.assertTrue(cartPage.areCartItemsVisible(), "Cart is empty before registration");

        cartPage.clickProceedToCheckout().clickRegisterLoginButton();

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
        String expectedCompany = createAccountPage.getCompanyName();
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
                        expectedFullName, expectedCompany, expectedAddress,
                        expectedCityStateZip, expectedCountry, expectedMobile),
                "Delivery address does not match registration info");

        Assert.assertTrue(checkoutPage.isBillingInfoMatching(
                        expectedFullName, expectedCompany, expectedAddress,
                        expectedCityStateZip, expectedCountry, expectedMobile),
                "Billing address does not match registration info");

        checkoutPage
                .enterMessage(DataUtility.getJsonData("ValidLoginData", "message"))
                .clickPlaceOrder()
                .enterNameOnCard(DataUtility.getJsonData("ValidLoginData", "nameOnCard"))
                .enterCardNumber(DataUtility.getJsonData("ValidLoginData", "cardNumber"))
                .enterCVC(DataUtility.getJsonData("ValidLoginData", "CVC"))
                .enterExpiryMonth(DataUtility.getJsonData("ValidLoginData", "ExpiryMonth"))
                .enterExpiryYear(DataUtility.getJsonData("ValidLoginData", "ExpiryYear"))
                .clickPayAndConfirmOrder();

        LogUtility.info("Order placed successfully");

        P09_PaymentDonePage invoicePage = new P09_PaymentDonePage(getDriver());
        invoicePage.clickDownloadInvoice();
        Assert.assertTrue(invoicePage.isFileDownloaded("C:\\Users\\moham\\Downloads", "invoice"), "❌ Invoice not downloaded");

        invoicePage.clickContinueShopping();

        header.clickDeleteAccount()
                .assertAccountDeletedMessageVisible()
                .clickContinueButton();

    }

}



