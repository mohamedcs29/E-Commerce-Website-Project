package Tests;

import Listeners.IInvokedMethodListenerImpl;
import Listeners.ITest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.P00_HeaderAndFooter;
import pages.P14_BrandsPage;
import utilities.DataUtility;
import utilities.LogUtility;

import static DriverFactory.DriverFactory.getDriver;

@Listeners({IInvokedMethodListenerImpl.class, ITest.class})

public class TC11_BrandProductTest extends TestBase {

    @Epic("AutomationExercise")
    @Feature("Brand Browsing")
    @Story("View and Cart Products by Brand")
    @Severity(SeverityLevel.NORMAL)
    @Description("""
            Test Case 19: View & Cart Brand Products
            1. Launch browser
            2. Navigate to url 'http://automationexercise.com'
            3. Click on 'Products' button
            4. Verify that Brands are visible on left side bar
            5. Click on any brand name
            6. Verify that user is navigated to brand page and brand products are displayed
            7. On left side bar, click on any other brand link
            8. Verify that user is navigated to that brand page and can see products
            """)
    @Test(description = "Verify that user can view and switch between brand product pages")
    public void verifyViewAndCartBrandProducts() {
        new P00_HeaderAndFooter(getDriver()).clickProducts();

        P14_BrandsPage brandsPage = new P14_BrandsPage(getDriver());
        Assert.assertTrue(brandsPage.isBrandsSidebarVisible(), "Brands sidebar not visible");

        brandsPage.clickOnBrandByName(DataUtility.getJsonData("ValidLoginData", "BrandName1"));

        Assert.assertTrue(brandsPage.getPageTitleText().contains("BRAND - POLO PRODUCTS"), "Brand page not displayed for Polo");

        brandsPage.clickOnBrandByName(DataUtility.getJsonData("ValidLoginData", "BrandName2"));

        Assert.assertTrue(brandsPage.getPageTitleText().contains("BRAND - BABYHUG PRODUCTS"), "Brand page not displayed for Babyhug");

        LogUtility.info("Brand product pages verified successfully.");
    }


}
