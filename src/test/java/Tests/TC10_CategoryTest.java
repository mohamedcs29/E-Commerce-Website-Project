package Tests;

import Listeners.IInvokedMethodListenerImpl;
import Listeners.ITest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.P13_CategoryPage;
import utilities.LogUtility;

import static DriverFactory.DriverFactory.getDriver;

@Epic("AutomationExercise")
@Listeners({IInvokedMethodListenerImpl.class, ITest.class})

public class TC10_CategoryTest extends TestBase {

    @Feature("Category Browsing")
    @Story("View Products by Category")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify user can view category products for Women and Men")
    @Description("""
            Test Case 18: View Category Products
            1. Launch browser
            2. Navigate to url 'http://automationexercise.com'
            3. Verify that categories are visible on left side bar
            4. Click on 'Women' category
            5. Click on any category link under 'Women' category, for example: Dress
            6. Verify that category page is displayed and confirm text 'WOMEN - DRESS PRODUCTS'
            7. On left side bar, click on any sub-category link of 'Men' category
            8. Verify that user is navigated to that category page
            """)
    public void verifyViewCategoryProducts() {

        P13_CategoryPage categoryPage = new P13_CategoryPage(getDriver());
        Assert.assertTrue(categoryPage.isCategorySidebarVisible(), "Categories sidebar not visible.");

        categoryPage.clickOnWomenCategory();
        categoryPage.clickOnDressSubCategory();

        Assert.assertTrue(categoryPage.isCategoryTitleContains("WOMEN - DRESS PRODUCTS"), "Incorrect title after selecting Women > Dress.");

        categoryPage.clickOnMenSubCategory();
        categoryPage.clickOnTShirtSubCategory();

        Assert.assertTrue(categoryPage.isCategoryTitleContains("MEN - TSHIRTS PRODUCTS"), "Incorrect title after selecting Men > Tshirts.");

        LogUtility.info("Category navigation verified successfully.");
    }


}
