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
public class EndToEndTest extends TestBase {
    @Test(description = "Valid Register User and Complete Purchase Flow")
    @Story("Register a new user, verify products, add to cart, checkout, and logout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Steps:
            1. Launch browser and navigate to home page
            2. Click on 'Signup/Login' button
            3. Verify 'New User Signup!' is visible
            4. Enter name and unique email
            5. Click 'Signup' and verify 'ENTER ACCOUNT INFORMATION'
            6. Fill details: Title, Password, DOB
            7. Select checkboxes for newsletter and offers
            8. Fill personal and address details
            9. Click 'Create Account'
            10. Verify account created, logged in, then logout
            11. Login with registered user
            12. Navigate to products page and verify product list
            13. Open product detail page for first product
            14. Add product quantity to cart and verify cart contents
            15. Proceed to checkout, verify delivery and billing addresses
            16. Place order with payment details
            17. Download invoice and logout
            """)
    public void testRegisterAndCompletePurchase() throws IOException {
        String email = "mohamed" + Utility.getTimeStamp() + "@gmail.com";

        LogUtility.info("Verifying home page URL");
        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );

        P02_CreateAccountPage filledAccountPage = new P00_HeaderAndFooter(getDriver())
                .clickSignupLogin()
                .assertNewUserSignupIsVisible()
                .enterUsername(DataUtility.getJsonData("ValidLoginData", "username"))
                .enterEmailSignUp(email)
                .clickSignupExpectingSuccess()
                .assertAccountInformationIsVisible("ENTER ACCOUNT INFORMATION")
                .clickOnRadioButton()
                .enterPassword(DataUtility.getJsonData("ValidLoginData", "password"))
                .checkNewsletter()
                .checkSpecialOffers()
                .selectDayDropdown(DataUtility.getJsonData("ValidLoginData", "days"))
                .selectMonthDropdown(DataUtility.getJsonData("ValidLoginData", "month"))
                .selectYearDropdown(DataUtility.getJsonData("ValidLoginData", "year"))
                .enterFirstName(DataUtility.getJsonData("ValidLoginData", "firstname"))
                .enterLastName(DataUtility.getJsonData("ValidLoginData", "lastname"))
                .enterCompany(DataUtility.getJsonData("ValidLoginData", "company"))
                .enterAddress(DataUtility.getJsonData("ValidLoginData", "address"))
                .selectDropDownCountry(DataUtility.getJsonData("ValidLoginData", "country"))
                .enterState(DataUtility.getJsonData("ValidLoginData", "state"))
                .enterCity(DataUtility.getJsonData("ValidLoginData", "city"))
                .enterZipcode(DataUtility.getJsonData("ValidLoginData", "zipcode"))
                .enterPhoneNumber(DataUtility.getJsonData("ValidLoginData", "phoneNumber"));

        String expectedFullName = filledAccountPage.getFullName();
        String expectedCompanyName = filledAccountPage.getCompanyName();
        String expectedAddress = filledAccountPage.getFullAddress();
        String expectedCityStateZip = filledAccountPage.getCityStateZip();
        String expectedCountry = filledAccountPage.getCountryName();
        String expectedMobile = filledAccountPage.getMobile();

        filledAccountPage.clickOnCreateAccountButton()
                .assertTextIsVisible("Account Created!")
                .clickOnContinueButton()
                .assertLoggedInUserTextIsVisible("Mohamed Hamdy")
                .clickLogout()
                .assertLoginTitleVisible()
                .enterEmailLogin(email)
                .enterPassWordLogin(DataUtility.getJsonData("ValidLoginData", "password"))
                .clickLoginExpectingSuccess()
                .assertLoggedInUserTextIsVisible("Mohamed Hamdy")
                .clickProducts()
                .assertTextProductsIsVisible("All Products")
                .productsListIsVisible()
                .clickViewProductByIndex(0);

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "product1_details_Url")),
                "User is not on product detail page"
        );

        Assert.assertTrue(
                new P12_ProductDetailsPage(getDriver()).isProductDetailsVisible(),
                "Product details are not visible correctly"
        );

        new P12_ProductDetailsPage(getDriver())
                .setQuantity(DataUtility.getJsonData("ValidLoginData", "quantityOfProduct"))
                .clickAddToCart()
                .clickViewCartFromModal();

        P06_CartPage cartPage = new P06_CartPage(getDriver());
        Assert.assertTrue(
                cartPage.verifyProductQuantityInCart(DataUtility.getJsonData("ValidLoginData", "quantityOfProduct")),
                "Product quantity is not correct in cart"
        );

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
        Assert.assertTrue(invoicePage.isFileDownloaded("C:\\Users\\moham\\Downloads", "invoice"), "Invoice not downloaded");

        invoicePage.clickContinueShopping();
        new P00_HeaderAndFooter(getDriver()).clickLogout();
        Assert.assertTrue(Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "LOGIN_Register_URL")));
    }


    @Test(description = "Purchase flow for existing user: login, search product, filter by brand, add recommended item, checkout, download invoice, logout")
    @Story("Login existing user, search product, filter by brand, add recommended item, checkout and logout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Steps:
            1. Launch browser and navigate to home page
            2. Login with existing user credentials
            3. Navigate to Products page
            4. Verify Products page is displayed
            5. Search for a product using search bar
            6. Verify search results are displayed
            7. Verify all search results match the search query
            8. Add all products from search results to cart
            9. Open cart and verify cart contents
            10. Proceed to checkout and verify delivery and billing addresses
            11. Place order with payment details
            12. Download invoice and verify file download
            13. Logout user and verify redirection to login page
            """)
    public void testExistingUserPurchaseWithSearchAndBrandFilter() throws IOException {
        P00_HeaderAndFooter header = new P00_HeaderAndFooter(getDriver());
        P01_Register_LoginPage loginPage = header.clickSignupLogin();
        loginPage.enterEmailLogin(DataUtility.getJsonData("ValidLoginData", "expectedEmailTc7"))
                .enterPassWordLogin(DataUtility.getJsonData("ValidLoginData", "password"))
                .clickLoginExpectingSuccess();

        P05_ProductsPage productsPage = header.clickProducts();
        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "product_URL")),
                "Not on All Products page"
        );

        productsPage.searchForProduct(
                DataUtility.getJsonData("ValidLoginData", "searchProduct"));
        Assert.assertTrue(productsPage.isSearchResultsDisplayed(), "Search results not visible");
        Assert.assertTrue(productsPage.areAllResultsMatching(DataUtility.getJsonData("ValidLoginData", "searchProductResult")), "Not all results match search");
        productsPage.addAllProductsToCart();
        P06_CartPage cartPage = header.clickCart();
        Assert.assertTrue(cartPage.areCartItemsVisible(), "Cart is empty after adding products");


        cartPage.
                assertCartPageIsVisible();

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


        LogUtility.info("Order placed successfully");
        P09_PaymentDonePage invoicePage = new P09_PaymentDonePage(getDriver());
        invoicePage.clickDownloadInvoice();
        Assert.assertTrue(invoicePage.isFileDownloaded("C:\\Users\\moham\\Downloads", "invoice"), "Invoice not downloaded");
        invoicePage.clickContinueShopping();
        new P00_HeaderAndFooter(getDriver()).clickLogout();
        Assert.assertTrue(Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "LOGIN_Register_URL")));

    }

    @Test(description = "Purchase flow for existing user: login, filter by brand, add to cart, checkout, download invoice, logout")
    @Story("Login existing user, filter by brand, add products to cart, checkout, download invoice, and logout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Steps:
            1. Launch browser and navigate to home page
            2. Login with existing user credentials
            3. Navigate to Products page
            4. Verify Products page is displayed
            5. Verify brands sidebar is visible
            6. Click on first brand and verify brand page title
            7. Click on second brand and verify brand page title
            8. Add all products from brand pages to cart
            9. Open cart and verify products are added
            10. Proceed to checkout and verify delivery and billing addresses
            11. Place order by entering payment details
            12. Download invoice and verify file download
            13. Logout user and verify redirection to login page
            """)
    public void testExistingUserPurchaseWithBrandFilter() throws IOException {
        P00_HeaderAndFooter header = new P00_HeaderAndFooter(getDriver());
        P01_Register_LoginPage loginPage = header.clickSignupLogin();

        loginPage.enterEmailLogin(DataUtility.getJsonData("ValidLoginData", "expectedEmailTc7"))
                .enterPassWordLogin(DataUtility.getJsonData("ValidLoginData", "password"))
                .clickLoginExpectingSuccess();

        P05_ProductsPage productsPage = header.clickProducts();
        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "product_URL")),
                "Not on All Products page"
        );

        P14_BrandsPage brandsPage = new P14_BrandsPage(getDriver());
        Assert.assertTrue(brandsPage.isBrandsSidebarVisible(), "Brands sidebar not visible");

        brandsPage.clickOnBrandByName(DataUtility.getJsonData("ValidLoginData", "BrandName1"));
        Assert.assertTrue(brandsPage.getPageTitleText().contains("BRAND - POLO PRODUCTS"), "Brand page not displayed for Polo");

        brandsPage.clickOnBrandByName(DataUtility.getJsonData("ValidLoginData", "BrandName2"));
        Assert.assertTrue(brandsPage.getPageTitleText().contains("BRAND - BABYHUG PRODUCTS"), "Brand page not displayed for Babyhug");

        LogUtility.info("Brand product pages verified successfully.");

        productsPage.addAllProductsToCart();

        P06_CartPage cartPage = header.clickCart();
        Assert.assertTrue(cartPage.areCartItemsVisible(), "Cart is empty after adding products");
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

        LogUtility.info("Order placed successfully");

        P09_PaymentDonePage invoicePage = new P09_PaymentDonePage(getDriver());
        invoicePage.clickDownloadInvoice();
        Assert.assertTrue(invoicePage.isFileDownloaded("C:\\Users\\moham\\Downloads", "invoice"), "Invoice not downloaded");
        invoicePage.clickContinueShopping();

        header.clickLogout();
        Assert.assertTrue(Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "LOGIN_Register_URL")));
    }

    @Feature("Place Order")
    @Story("Register while Checkout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            Test Case 14: Place Order: Register while Checkout
            
            1. Launch browser and navigate to 'http://automationexercise.com'
            2. Verify that home page is visible successfully
            3. Navigate to 'Products' page
            4. Add two products to cart by index
            5. Click 'Cart' button
            6. Verify cart page is displayed
            7. Click 'Proceed To Checkout' button
            8. Click 'Register / Login' button on checkout page
            9. Fill all signup details and register a new account
            10. Verify 'ACCOUNT CREATED!' message and click 'Continue'
            11. Verify 'Logged in as username' at top
            12. Click 'Cart' button again
            13. Click 'Proceed To Checkout' button
            14. Verify Delivery and Billing address details match registered data
            15. Verify total amount is correct
            16. Enter comment/message in the text area and click 'Place Order'
            17. Enter payment details: Name on Card, Card Number, CVC, Expiration Date
            18. Click 'Pay and Confirm Order' button
            19. Click 'Delete Account' button
            20. Verify 'ACCOUNT DELETED!' message and click 'Continue'
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
    @Story("Login before Checkout")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
            1. Launch browser and navigate to 'http://automationexercise.com'
            2. Verify that home page is visible successfully
            3. Click 'Signup / Login' button
            4. Fill in email and password, then click 'Login' button
            5. Verify 'Logged in as username' is visible at top
            6. Verify 'Recommended Items' section is visible
            7. Add first recommended item to cart
            8. Click 'Cart' button
            9. Verify cart page is displayed with the added product
            10. Click 'Proceed To Checkout' button
            11. Verify Delivery and Billing address details match expected user data
            12. Enter comment/message in the text area and click 'Place Order'
            13. Enter payment details: Name on Card, Card Number, CVC, Expiration Date
            14. Click 'Pay and Confirm Order' button
            15. (Optional) Delete account and verify deletion message
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

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );

        P04_HomeAfterLogin recommended = new P04_HomeAfterLogin(getDriver());
        Assert.assertTrue(recommended.isRecommendedSectionVisible(), "Recommended section not visible");

        recommended.addFirstRecommendedItemToCart();

        recommended.clickViewCartButton();

        P06_CartPage cartPage = new P06_CartPage(getDriver());
        Assert.assertTrue(cartPage.areCartItemsVisible(), "Product not found in cart from recommended section");


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


    @Feature("Contact Us Form")
    @Story("User registers, logs in, fills and submits contact us form")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            Steps:
            1. Launch browser and navigate to home page
            2. Verify that home page URL is correct
            3. Go to Signup/Login page
            4. Sign up a new user with unique email
            5. Fill in account information and create account
            6. Verify 'Account Created!' message is displayed
            7. Continue to home page and verify user is logged in
            8. Click on 'Contact Us' button
            9. Verify 'GET IN TOUCH' section is visible
            10. Enter name, email, subject, and message
            11. Upload a file
            12. Click 'Submit' and accept confirmation alert
            13. Verify success message 'Success! Your details have been submitted successfully.' is visible
            14. Click 'Home' button
            15. Delete account and verify 'Account Deleted!' message
            16. Continue to home page
            """)

    @Test(description = "Verify user can submit contact form successfully")
    public void verifyNewUserContactUsAndAccountDeletion() throws IOException {
        LogUtility.info("Verifying home page URL");
        String email = "mohamed" + Utility.getTimeStamp() + "@gmail.com";

        LogUtility.info("Verifying home page URL");
        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );

        new P00_HeaderAndFooter(getDriver())
                .clickSignupLogin()
                .assertNewUserSignupIsVisible()
                .enterUsername(DataUtility.getJsonData("ValidLoginData", "username"))
                .enterEmailSignUp(email)
                .clickSignupExpectingSuccess()
                .assertAccountInformationIsVisible("ENTER ACCOUNT INFORMATION")
                .clickOnRadioButton()
                .enterPassword(DataUtility.getJsonData("ValidLoginData", "password"))
                .checkNewsletter()
                .checkSpecialOffers()
                .selectDayDropdown(DataUtility.getJsonData("ValidLoginData", "days"))
                .selectMonthDropdown(DataUtility.getJsonData("ValidLoginData", "month"))
                .selectYearDropdown(DataUtility.getJsonData("ValidLoginData", "year"))
                .enterFirstName(DataUtility.getJsonData("ValidLoginData", "firstname"))
                .enterLastName(DataUtility.getJsonData("ValidLoginData", "lastname"))
                .enterCompany(DataUtility.getJsonData("ValidLoginData", "company"))
                .enterAddress(DataUtility.getJsonData("ValidLoginData", "address"))
                .selectDropDownCountry(DataUtility.getJsonData("ValidLoginData", "country"))
                .enterState(DataUtility.getJsonData("ValidLoginData", "state"))
                .enterCity(DataUtility.getJsonData("ValidLoginData", "city"))
                .enterZipcode(DataUtility.getJsonData("ValidLoginData", "zipcode"))
                .enterPhoneNumber(DataUtility.getJsonData("ValidLoginData", "phoneNumber"))
                .clickOnCreateAccountButton()
                .assertTextIsVisible("Account Created!")
                .clickOnContinueButton()
                .assertLoggedInUserTextIsVisible("Mohamed Hamdy")
                .clickContactUs()
                .assertGetInTouchIsVisible()
                .enterName(DataUtility.getJsonData("ValidLoginData", "username"))
                .enterEmail(email)
                .enterSubject("test")
                .enterMessage("test contact thanks")
                .uploadFile(System.getProperty("user.dir") + DataUtility.getPropertyValue("environment", "pathFile"))
                .clickSubmitButton()
                .acceptAlertAfterSubmit()
                .assertSuccessMessageIsVisible()
                .clickHomeButton()
                .clickDeleteAccount()
                .assertAccountDeletedMessageVisible()
                .clickContinueButton();
        Assert.assertTrue(Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "Home_URL_Expected")));


    }


}





