package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utilities.Utility;

public class P08_PaymentPage {
    private final WebDriver driver;
    private final By nameOnCardInput = By.cssSelector("input[data-qa='name-on-card']");
    private final By cardNumberInput = By.cssSelector("input[data-qa='card-number']");
    private final By cvcInput = By.cssSelector("input[data-qa='cvc']");
    private final By expiryMonthInput = By.cssSelector("input[data-qa='expiry-month']");
    private final By expiryYearInput = By.cssSelector("input[data-qa='expiry-year']");
    private final By payAndConfirmButton = By.cssSelector("button#submit");      // زر الدفع

    public P08_PaymentPage(WebDriver driver) {

        this.driver = driver;
    }

    public P08_PaymentPage enterNameOnCard(String name) {

        Utility.sendData(driver, nameOnCardInput, name);
        return this;
    }

    public P08_PaymentPage enterCardNumber(String number) {

        Utility.sendData(driver, cardNumberInput, number);
        return this;
    }

    public P08_PaymentPage enterCVC(String cvc) {

        Utility.sendData(driver, cvcInput, cvc);
        return this;
    }

    public P08_PaymentPage enterExpiryMonth(String month) {

        Utility.sendData(driver, expiryMonthInput, month);
        return this;

    }

    public P08_PaymentPage enterExpiryYear(String year) {

        Utility.sendData(driver, expiryYearInput, year);
        return this;

    }

    public P09_PaymentDonePage clickPayAndConfirmOrder() {
        Utility.clickOnElement(driver, payAndConfirmButton);
        return new P09_PaymentDonePage(driver);
    }


}
