package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Utility;

import java.time.Duration;

import static utilities.Utility.verifyText;

public class P02_CreateAccountPage {
    private final WebDriver driver;
    private final By password = By.cssSelector("input[type='password'][id='password']");
    private final By firstName = By.cssSelector("input[data-qa='first_name']");
    private final By lastName = By.cssSelector("input[id='last_name']");
    private final By address = By.cssSelector("input[id='address1']");
    private final By dropDownCountry = By.cssSelector("select[id='country']");
    private final By state = By.cssSelector("input[id='state']");
    private final By city = By.cssSelector("input[id='city']");
    private final By zipcode = By.cssSelector("input[id='zipcode']");
    private final By mobileNumber = By.cssSelector("input[id='mobile_number']");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By newsletterCheckbox = By.cssSelector("input#newsletter");
    private final By specialOffersCheckbox = By.cssSelector("input#optin");
    private final By accountInformationTitle = By.xpath("//b[contains(text(),'Account Information')]");
    private final By dayDropdown = By.id("days");
    private final By monthDropdown = By.id("months");
    private final By yearDropdown = By.id("years");
    private final By company = By.cssSelector("input[data-qa='company']");
    private final By radioButton = By.xpath("//input[@type='radio' and @value='Mr']"); // أكثر دقة


    // Data storage
    private String fullName;
    private String companyName;
    private String fullAddress;
    private String cityText;
    private String stateText;
    private String zipText;
    private String countryName;
    private String mobile;

    public P02_CreateAccountPage(WebDriver driver) {
        this.driver = driver;
    }

    public P02_CreateAccountPage assertAccountInformationIsVisible(String text) {
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(accountInformationTitle));

        verifyText(driver, accountInformationTitle, text);
        return this;
    }

    public P02_CreateAccountPage enterCompany(String COMPANY) {
        Utility.sendData(driver, company, COMPANY);
        this.companyName = COMPANY;
        return this;
    }

    public P02_CreateAccountPage clickOnRadioButton() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(radioButton));
        Utility.clickOnElement(driver, radioButton);
        return this;
    }


    public P02_CreateAccountPage enterPassword(String PASSWORD) {
        Utility.sendData(driver, password, PASSWORD);
        return this;
    }

    public P02_CreateAccountPage checkNewsletter() {
        Utility.clickOnElement(driver, newsletterCheckbox);
        return this;
    }

    public P02_CreateAccountPage checkSpecialOffers() {
        Utility.clickOnElement(driver, specialOffersCheckbox);
        return this;
    }

    public P02_CreateAccountPage selectDayDropdown(String VALUE) {
        Utility.selectingFromDropDown(driver, dayDropdown, VALUE);
        return this;
    }

    public P02_CreateAccountPage selectMonthDropdown(String VALUE) {
        Utility.selectingFromDropDown(driver, monthDropdown, VALUE);
        return this;
    }

    public P02_CreateAccountPage selectYearDropdown(String VALUE) {
        Utility.selectingFromDropDown(driver, yearDropdown, VALUE);
        return this;
    }

    public P02_CreateAccountPage enterFirstName(String FIRSTNAME) {
        Utility.sendData(driver, firstName, FIRSTNAME);
        this.fullName = FIRSTNAME;
        return this;
    }

    public P02_CreateAccountPage enterLastName(String LASTNAME) {
        Utility.sendData(driver, lastName, LASTNAME);
        this.fullName = this.fullName + " " + LASTNAME;
        return this;
    }


    public P02_CreateAccountPage enterAddress(String ADDRESS) {
        Utility.sendData(driver, address, ADDRESS);
        this.fullAddress = ADDRESS;
        return this;
    }

    public P02_CreateAccountPage enterCity(String CITY) {
        Utility.sendData(driver, city, CITY);
        this.cityText = CITY;
        return this;
    }

    public P02_CreateAccountPage enterState(String STATE) {
        Utility.sendData(driver, state, STATE);
        this.stateText = STATE;
        return this;
    }

    public P02_CreateAccountPage enterZipcode(String ZIPCODE) {
        Utility.sendData(driver, zipcode, ZIPCODE);
        this.zipText = ZIPCODE;
        return this;
    }

    public P02_CreateAccountPage selectDropDownCountry(String VALUE) {
        Utility.selectingFromDropDown(driver, dropDownCountry, VALUE);
        this.countryName = VALUE;
        return this;
    }

    public P02_CreateAccountPage enterPhoneNumber(String PHONENUMBER) {
        Utility.sendData(driver, mobileNumber, PHONENUMBER);
        this.mobile = PHONENUMBER;
        return this;
    }

    public P03_ConfirmationRegisterPage clickOnCreateAccountButton() {
        Utility.clickOnElement(driver, submitButton);
        return new P03_ConfirmationRegisterPage(driver);
    }

    public String getFullName() {
        return fullName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getCityStateZip() {
        return String.join(" ", cityText, stateText, zipText).trim();
    }

    public String getCountryName() {
        return countryName;
    }

    public String getMobile() {
        return mobile;
    }
}
