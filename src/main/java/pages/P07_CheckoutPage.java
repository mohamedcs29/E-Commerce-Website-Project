package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utilities.Utility;

import java.util.List;

public class P07_CheckoutPage {
    private final WebDriver driver;

    private final By deliveryFullName = By.xpath("//ul[@id='address_delivery']/li[@class='address_firstname address_lastname']");
    private final By deliveryCompany = By.xpath("//ul[@id='address_delivery']/li[3]"); // Company name is not present in this view, so we'll adjust logic
    private final By deliveryAddress = By.xpath("//ul[@id='address_delivery']/li[4]");
    private final By deliveryCityStateZip = By.xpath("//ul[@id='address_delivery']/li[@class='address_city address_state_name address_postcode']");
    private final By deliveryCountry = By.xpath("//ul[@id='address_delivery']/li[@class='address_country_name']");
    private final By deliveryMobile = By.xpath("//ul[@id='address_delivery']/li[@class='address_phone']");

    private final By billingFullName = By.xpath("//ul[@id='address_invoice']/li[@class='address_firstname address_lastname']");
    private final By billingCompany = By.xpath("//ul[@id='address_invoice']/li[3]"); // Adjust logic for company
    private final By billingAddress = By.xpath("//ul[@id='address_invoice']/li[4]");
    private final By billingCityStateZip = By.xpath("//ul[@id='address_invoice']/li[@class='address_city address_state_name address_postcode']");
    private final By billingCountry = By.xpath("//ul[@id='address_invoice']/li[@class='address_country_name']");
    private final By billingMobile = By.xpath("//ul[@id='address_invoice']/li[@class='address_phone']");

    private final By placeOrderButton = By.cssSelector("a.btn.check_out[href='/payment']");
    private final By message = By.cssSelector("textarea[name='message']");


    public P07_CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getDeliveryFullName() {
        return driver.findElement(deliveryFullName).getText().trim().replaceFirst("Mr\\.\\s*|Mrs\\.\\s*", "");
    }

    public String getDeliveryCompany() {
        return driver.findElement(deliveryCompany).getText().trim();
    }

    public String getDeliveryAddress() {
        return driver.findElement(deliveryAddress).getText().trim();
    }

    public String getDeliveryCityStateZip() {
        return driver.findElement(deliveryCityStateZip).getText().trim();
    }

    public String getDeliveryCountry() {
        return driver.findElement(deliveryCountry).getText().trim();
    }

    public String getDeliveryMobile() {
        return driver.findElement(deliveryMobile).getText().trim();
    }

    public String getBillingFullName() {
        return driver.findElement(billingFullName).getText().trim().replaceFirst("Mr\\.\\s*|Mrs\\.\\s*", "");
    }

    public String getBillingCompany() {
        return driver.findElement(billingCompany).getText().trim();

    }

    public String getBillingAddress() {
        return driver.findElement(billingAddress).getText().trim();
    }

    public String getBillingCityStateZip() {
        return driver.findElement(billingCityStateZip).getText().trim();
    }

    public String getBillingCountry() {
        return driver.findElement(billingCountry).getText().trim();
    }

    public String getBillingMobile() {
        return driver.findElement(billingMobile).getText().trim();
    }

    public boolean isDeliveryInfoMatching(String fullName, String company, String address,
                                          String cityStateZip, String country, String mobile) {
        boolean companyMatches = (company == null || company.isEmpty()) ? true : getDeliveryCompany().equals(company);
        return getDeliveryFullName().equals(fullName) &&
                companyMatches &&
                getDeliveryAddress().equals(address) &&
                getDeliveryCityStateZip().equals(cityStateZip) &&
                getDeliveryCountry().equals(country) &&
                getDeliveryMobile().equals(mobile);
    }

    public boolean isBillingInfoMatching(String fullName, String company, String address,
                                         String cityStateZip, String country, String mobile) {
        boolean companyMatches = (company == null || company.isEmpty()) ? true : getBillingCompany().equals(company);

        return getBillingFullName().equals(fullName) &&
                companyMatches &&
                getBillingAddress().equals(address) &&
                getBillingCityStateZip().equals(cityStateZip) &&
                getBillingCountry().equals(country) &&
                getBillingMobile().equals(mobile);
    }


    public double calculateExpectedTotalAmount() {
        double total = 0.0;

        List<WebElement> allPrices = driver.findElements(By.cssSelector("td.cart_price > p"));
        List<WebElement> allQuantities = driver.findElements(By.cssSelector("td.cart_quantity > button.disabled"));

        for (int i = 0; i < allPrices.size(); i++) {
            String priceText = allPrices.get(i).getText().replaceAll("[^0-9.]", "");
            String quantityText = allQuantities.get(i).getText();

            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);

            System.out.println("Product " + (i + 1) + " → price: " + price + " × quantity: " + quantity);

            total += price * quantity;
        }

        System.out.println("Calculated expected total: " + total);
        return total;
    }


    public double getTotalAmountDisplayed() {
        double total = 0.0;

        List<WebElement> totals = driver.findElements(By.cssSelector("tbody > tr:last-child > td > p.cart_total_price"));

        for (WebElement el : totals) {
            String raw = el.getText().replaceAll("[^0-9.]", "");
            total += Double.parseDouble(raw);
        }

        System.out.println("Calculated Displayed Total (review-payment only): " + total);
        return total;
    }


    public boolean isTotalAmountCorrect() {
        double displayed = getTotalAmountDisplayed();
        double expected = calculateExpectedTotalAmount();

        System.out.println("Displayed Total: " + displayed);
        System.out.println("Expected Total : " + expected);

        return Math.abs(displayed - expected) < 0.01;
    }

    public P07_CheckoutPage enterMessage(String messageText) {
        Utility.sendData(driver, message, messageText);
        return this;

    }

    public P08_PaymentPage clickPlaceOrder() {
        Utility.clickOnElement(driver, placeOrderButton);
        return new P08_PaymentPage(driver);
    }


}
