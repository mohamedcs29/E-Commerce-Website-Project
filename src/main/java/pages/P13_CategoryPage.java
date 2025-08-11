package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Utility;

import java.time.Duration;

public class P13_CategoryPage {
    private final WebDriver driver;
    private final By categoriesTitle = By.xpath("//h2[text()='Category']");
    private final By womenCategory = By.xpath("//a[@href='#Women']");
    private final By dressSubCategory = By.xpath("//a[@href='/category_products/1']"); // حسب ID المأخوذ من الموقع
    private final By categoryPageTitle = By.cssSelector("h2.title.text-center");
    private final By menSubCategory = By.cssSelector("a[href='#Men'] span[class='badge pull-right']"); // مثال على men tshirts
    private final By tShirtSubCategory = By.cssSelector("a[href='/category_products/3']"); // حسب ID المأخوذ من الموقع

    public P13_CategoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isCategorySidebarVisible() {
        return driver.findElement(categoriesTitle).isDisplayed();
    }

    public void clickOnWomenCategory() {
        Utility.clickOnElement(driver, womenCategory);
    }

    public void clickOnDressSubCategory() {
        Utility.clickOnElement(driver, dressSubCategory);
    }

    public boolean isCategoryTitleContains(String expectedText) {
        String actual = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(categoryPageTitle))
                .getText().trim();
        return actual.equalsIgnoreCase(expectedText);
    }

    public void clickOnMenSubCategory() {
        Utility.clickOnElement(driver, menSubCategory);
    }

    public void clickOnTShirtSubCategory() {
        Utility.clickOnElement(driver, tShirtSubCategory);
    }
}
