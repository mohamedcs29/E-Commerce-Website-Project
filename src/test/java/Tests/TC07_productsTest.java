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

public class TC07_productsTest extends TestBase {

    @Feature("Product Page")
    @Story("Verify All Products and Product Detail Page")
    @Description("""
            Steps:
            1. Launch browser
            2. Navigate to url 'http://automationexercise.com'
            3. Verify that home page is visible successfully
            4. Click on 'Products' button
            5. Verify user is navigated to ALL PRODUCTS page successfully
            6. The products list is visible
            7. Click on 'View Product' of first product
            8. User is landed to product detail page
            9. Verify product details: name, category, price, availability, condition, brand
            """)
    @Test
    public void verifyProductDetail() throws IOException {

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );

        new P00_HeaderAndFooter(getDriver())
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

    }

    @Feature("Product Page")
    @Story("Search Product")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            Steps:
            1. Launch browser
            2. Navigate to url 'http://automationexercise.com'
            3. Verify that home page is visible successfully
            4. Click on 'Products' button
            5. Verify user is navigated to ALL PRODUCTS page successfully
            6. Enter product name in search input and click search button
            7. Verify 'SEARCHED PRODUCTS' is visible
            8. Verify all the products related to search are visible
            """)
    @Test
    public void verifySearchProduct() throws IOException {

        LogUtility.info("Verifying home page URL");
        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );

        new P00_HeaderAndFooter(getDriver())
                .clickProducts()
                .assertTextProductsIsVisible("All Products")
                .searchForProduct(DataUtility.getJsonData("ValidLoginData", "searchProduct"));

        Assert.assertTrue(
                new P05_ProductsPage(getDriver()).isSearchResultsDisplayed(),
                "'SEARCHED PRODUCTS' is not visible"
        );

        Assert.assertTrue(
                new P05_ProductsPage(getDriver()).areAllResultsMatching(DataUtility.getJsonData("ValidLoginData", "searchProductResult")),
                "Search results do not match expected keyword"
        );

    }

    @Epic("AutomationExercise")
    @Feature("Products Functionality")
    @Story("Verify search for a product that does not exist")
    @Description("""
                Steps:
                1. Launch browser
                2. Navigate to url 'http://automationexercise.com'
                3. Verify that home page is visible successfully
                4. Click 'Products' button
                5. Verify user is navigated to 'ALL PRODUCTS' page successfully
                6. Enter a product name that does not exist (e.g., 'XYZProduct123') in search input
                7. Click 'Search' button
                8. Verify that no products are displayed in the results
            """)
    @Test
    public void verifySearchForProductDoesNotExist() throws IOException {
        LogUtility.info("Verifying home page URL");
        Assert.assertTrue(
                Utility.isCurrentUrlEquals(
                        getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")
                ),
                "Home page is not visible."
        );

        new P00_HeaderAndFooter(getDriver())
                .clickProducts()
                .assertTextProductsIsVisible("All Products")
                .searchForProduct(DataUtility.getJsonData("ValidLoginData", "invalidProduct"));

        Assert.assertTrue(
                new P05_ProductsPage(getDriver()).isSearchResultsEmpty(),
                "Expected no products but some were displayed."
        );
    }


    @Feature("Cart Functionality")
    @Story("Add Multiple Products to Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("""
                Steps:
                1. Launch browser
                2. Navigate to url 'http://automationexercise.com'
                3. Verify that home page is visible successfully
                4. Click 'Products' button
                5. Hover over first product and click 'Add to cart'
                6. Click 'Continue Shopping' button
                7. Hover over second product and click 'Add to cart'
                8. Click 'View Cart' button
                9. Verify both products are added to Cart
                10. Verify their prices, quantity and total price
            """)
    @Test
    public void verifyAddMultipleProductsToCartAndValidate() throws IOException {

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

        Assert.assertTrue(
                cartPage.areProductQuantitiesCorrect(productsPage.getAddedProductCounts()),
                "Quantities of products in cart are incorrect"
        );

        Assert.assertTrue(
                cartPage.isEachProductTotalCorrect(),
                "Price × Quantity does not equal Total for one or more products"
        );

    }


    @Feature("Product Search and Cart Persistence")
    @Story("Search for products, add to cart, and verify cart persistence after login")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            Test Case 20: Search Products and Verify Cart After Login
            1. Launch browser
            2. Navigate to url 'http://automationexercise.com'
            3. Click on 'Products' button
            4. Verify user is navigated to ALL PRODUCTS page successfully
            5. Enter product name in search input and click search button
            6. Verify 'SEARCHED PRODUCTS' is visible
            7. Verify all the products related to search are visible
            8. Add those products to cart
            9. Click 'Cart' button and verify that products are visible in cart
            10. Click 'Signup / Login' button and submit login details
            11. Again, go to Cart page
            12. Verify that those products are visible in cart after login as well
            """)
    @Test(description = "Verify search, add to cart, and cart persistence after login")
    public void verifySearchAndCartAfterLogin() throws IOException {

        P00_HeaderAndFooter header = new P00_HeaderAndFooter(getDriver());
        P05_ProductsPage productsPage = header.clickProducts();

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(), DataUtility.getPropertyValue("environment", "product_URL")),
                "Not on All Products page"
        );

        productsPage.searchForProduct(DataUtility.getJsonData("ValidLoginData", "searchProduct"));

        Assert.assertTrue(productsPage.isSearchResultsDisplayed(), "Search results not visible");

        Assert.assertTrue(productsPage.areAllResultsMatching(DataUtility.getJsonData("ValidLoginData", "searchProductResult")), "Not all results match search");

        productsPage.addAllProductsToCart();

        P06_CartPage cartPage = header.clickCart();
        Assert.assertTrue(cartPage.areCartItemsVisible(), "Cart is empty before login");

        P01_Register_LoginPage loginPage = header.clickSignupLogin();
        loginPage.enterEmailLogin(DataUtility.getJsonData("ValidLoginData", "expectedEmailTc7"))
                .enterPassWordLogin(DataUtility.getJsonData("ValidLoginData", "password"))
                .clickLoginExpectingSuccess();

        cartPage = header.clickCart();

        Assert.assertTrue(cartPage.areCartItemsVisible(), "Cart items not preserved after login");

    }


    @Feature("Product Review")
    @Story("Add a review to a product")
    @Severity(SeverityLevel.MINOR)
    @Description("""
            Test Case 21: Add Review on Product
            1. Launch browser
            2. Navigate to url 'http://automationexercise.com'
            3. Click on 'Products' button
            4. Verify user is navigated to ALL PRODUCTS page successfully
            5. Click on 'View Product' button
            6. Verify 'Write Your Review' is visible
            7. Enter name, email and review
            8. Click 'Submit' button
            9. Verify success message 'Thank you for your review.'
            """)
    @Test(description = "Verify user can submit a review for a product successfully")
    public void verifyAddReviewOnProduct() throws IOException {

        Assert.assertTrue(
                Utility.isCurrentUrlEquals(getDriver(),
                        DataUtility.getPropertyValue("environment", "Home_URL_Expected")),
                "Home page is not visible."
        );

        P00_HeaderAndFooter header = new P00_HeaderAndFooter(getDriver());
        P05_ProductsPage productsPage = header.clickProducts();

        productsPage.assertTextProductsIsVisible("All Products");

        P12_ProductDetailsPage productDetailsPage = productsPage.clickViewProductByIndex(0);

        Assert.assertTrue(productDetailsPage.isReviewSectionVisible(), "Review section not visible");

        productDetailsPage.enterReviewName(DataUtility.getJsonData("ValidLoginData", "username"))
                .enterReviewEmail(DataUtility.getJsonData("ValidLoginData", "expectedEmail"))
                .enterReviewText(DataUtility.getJsonData("ValidLoginData", "expectedEmail"));

        productDetailsPage.submitReview();

        Assert.assertTrue(productDetailsPage.isReviewSuccessMessageVisible(), "Success message not visible");

    }


    @Feature("Cart Functionality")
    @Story("Add to cart from Recommended items")
    @Severity(SeverityLevel.MINOR)
    @Description("Test Case 22: Add to cart from Recommended items")
    @Test
    public void verifyAddToCartFromRecommendedItems() throws IOException {

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

    }

}



