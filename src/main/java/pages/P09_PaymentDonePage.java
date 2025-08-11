package pages;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.LogUtility;

import java.io.File;
import java.time.Duration;

public class P09_PaymentDonePage {
    private final WebDriver driver;
    private final By downloadInvoiceButton = By.cssSelector("a.btn[href*='download_invoice']");
    private final By continueShoppingButton = By.cssSelector("a.btn[href='/']");

    private final WebDriverWait wait;
    String downloadDir = System.getProperty("user.dir") + "/downloads";

    public P09_PaymentDonePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }

    public void clickDownloadInvoice() {
        driver.findElement(downloadInvoiceButton).click();
    }

    public P04_HomeAfterLogin clickContinueShopping() {
        driver.findElement(continueShoppingButton).click();
        return new P04_HomeAfterLogin(driver);
    }

    public boolean isFileDownloaded(String downloadPath, String partialFileName) {
        LogUtility.info("Verifying file download using Awaitility...");

        try {
            Awaitility.await()
                    .atMost(Duration.ofSeconds(20))
                    .pollInterval(Duration.ofSeconds(1))
                    .ignoreExceptions()
                    .until(() -> {
                        File downloadDir = new File(downloadPath);
                        File[] files = downloadDir.listFiles();
                        for (File file : files) {
                            if (file.getName().contains(partialFileName) && !file.getName().endsWith(".crdownload")) {
                                return true;
                            }
                        }
                        return false;
                    });

            LogUtility.info("File found successfully within the time limit.");
            return true;

        } catch (Exception e) {
            LogUtility.error("File was not downloaded within the specified timeout.");
            return false;
        }
    }


}
