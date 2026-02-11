package stepdefinitions;

import base.DriverSetup;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.LoginPage;
import pages.LogoutPage;

public class LogoutSteps {

    private final WebDriver driver;
    private final LoginPage loginPage;
    private final LogoutPage logoutPage;

    public LogoutSteps() {
        this.driver = DriverSetup.getDriver();
        this.loginPage = new LoginPage(driver);
        this.logoutPage = new LogoutPage(driver);
    }

    private boolean isSidePanelVisible() {
        try {
            return driver.findElements(By.cssSelector("aside.oxd-sidepanel")).size() > 0
                    || driver.findElements(By.xpath("//span[normalize-space()='Dashboard']")).size() > 0;
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

    @Given("I am logged in to OrangeHRM")
    public void i_am_logged_in_to_orangehrm() {
        ensureLoggedIn();
        Assert.assertTrue(isSidePanelVisible(), "Not logged in.");
    }

    @When("I logout from the application")
    public void i_logout_from_the_application() {
        logoutPage.logout();
    }

    @Then("I should be redirected to login page")
    public void i_should_be_redirected_to_login_page() {
        Assert.assertTrue(logoutPage.isOnLoginPage(), "Logout failed - login page not visible.");
    }
}
