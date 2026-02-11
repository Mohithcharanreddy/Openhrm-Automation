package stepdefinitions;

import base.DriverSetup;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.LoginPage;
import pages.MyInfoPage;

public class MyInfoSteps {

    private final WebDriver driver;
    private final LoginPage loginPage;
    private final MyInfoPage myInfoPage;

    private static String savedOtherId;
    private static String savedMobile;

    public MyInfoSteps() {
        this.driver = DriverSetup.getDriver();
        this.loginPage = new LoginPage(driver);
        this.myInfoPage = new MyInfoPage(driver);
    }

    private boolean isSidePanelVisible() {
        try {
            return driver.findElements(By.cssSelector("aside.oxd-sidepanel")).size() > 0
                    || driver.findElements(By.xpath("//span[normalize-space()='Dashboard']")).size() > 0
                    || driver.findElements(By.xpath("//span[normalize-space()='My Info']")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void ensureLoggedIn() {
        if (isSidePanelVisible()) return;

        loginPage.openLoginPage();
        loginPage.login("admin", "admin123");
        Assert.assertTrue(loginPage.isLoggedIn(), "Login failed - side panel/dashboard not visible.");
    }

    @Given("I am logged in and on My Info page")
    public void i_am_logged_in_and_on_my_info_page() {
        ensureLoggedIn();
        myInfoPage.openMyInfo();
        Assert.assertTrue(myInfoPage.isPersonalDetailsDisplayed() || myInfoPage.isContactDetailsDisplayed(),
                "My Info page not opened.");
    }

    @When("I update personal details")
    public void i_update_personal_details() {
        // Use stable field Other Id
        savedOtherId = "OID_" + System.currentTimeMillis();
        myInfoPage.updatePersonalDetailsOtherId(savedOtherId);
    }

    @Then("the personal details should be saved")
    public void the_personal_details_should_be_saved() {
        // Re-open and verify value persisted (simple, not toast-dependent)
        myInfoPage.openMyInfo();
        String actual = myInfoPage.getPersonalDetailsOtherIdValue();
        Assert.assertTrue(actual != null && actual.trim().equals(savedOtherId),
                "Personal details not saved. Expected Other Id: " + savedOtherId + " but got: " + actual);
    }

    @When("I update contact details")
    public void i_update_contact_details() {
        myInfoPage.openMyInfo();
        myInfoPage.openContactDetails();

        savedMobile = "9" + String.valueOf(System.currentTimeMillis()).substring(3, 12); // 10 digits-ish
        myInfoPage.updateContactMobile(savedMobile);
    }

    @Then("the contact details should be saved")
    public void the_contact_details_should_be_saved() {
        myInfoPage.openMyInfo();
        myInfoPage.openContactDetails();

        String actual = myInfoPage.getContactMobileValue();
        Assert.assertTrue(actual != null && actual.trim().equals(savedMobile),
                "Contact details not saved. Expected Mobile: " + savedMobile + " but got: " + actual);
    }
}
