package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utilities.Utility;

public class P14_BrandsPage {

    private final WebDriver driver;
    private final By brandsSidebar = By.cssSelector(".brands_products");
    private final By brandPageTitle = By.cssSelector(".title.text-center");

    public P14_BrandsPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isBrandsSidebarVisible() {
        return driver.findElement(brandsSidebar).isDisplayed();
    }

    public void clickOnBrandByName(String brandName) {
        By brandLink = By.cssSelector("a[href='/brand_products/" + brandName + "']");

        Utility.scrolling(driver, brandLink);

        Utility.clickOnElement(driver, brandLink);
    }

    public String getPageTitleText() {
        return Utility.getElementText(driver, brandPageTitle);
    }
}
