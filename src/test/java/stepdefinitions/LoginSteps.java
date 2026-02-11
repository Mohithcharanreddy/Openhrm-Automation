package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.LoginPage;

import java.time.Duration;

public class LoginSteps {

    private LoginPage loginPage;

    @Given("I am on the OrangeHRM login page")
    public void i_am_on_the_orange_hrm_login_page() {
        // Initialize AFTER Hooks
        loginPage = new LoginPage(Hooks.driver);

        Hooks.driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        WebDriverWait wait = new WebDriverWait(Hooks.driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
    }

    @When("I enter valid username {string} and password {string}")
    public void i_enter_valid_username_and_password(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @When("I enter invalid username {string} and password {string}")
    public void i_enter_invalid_username_and_password(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @When("I enter valid username {string} and invalid password {string}")
    public void i_enter_valid_username_and_invalid_password(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @When("I enter invalid username {string} and invalid password {string}")
    public void i_enter_invalid_username_and_invalid_password(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @When("I click on the login button")
    public void i_click_on_the_login_button() {
        loginPage.clickLogin();
    }

    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
        WebDriverWait wait = new WebDriverWait(Hooks.driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".oxd-topbar-header")));
        Assert.assertTrue(Hooks.driver.getCurrentUrl().contains("/dashboard"));
    }

    @Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String expectedMessage) {
        WebDriverWait wait = new WebDriverWait(Hooks.driver, Duration.ofSeconds(20));
        String actual;

        if (expectedMessage.equals("Required")) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("span.oxd-input-field-error-message")));
            actual = loginPage.getRequiredErrorMessage();
        } else {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("p.oxd-alert-content-text")));
            actual = loginPage.getAlertErrorMessage();
        }

        Assert.assertEquals(actual, expectedMessage);
    }

    @Then("the username field should be visible")
    public void the_username_field_should_be_visible() {
        Assert.assertTrue(Hooks.driver.findElement(By.name("username")).isDisplayed());
    }

    @Then("the password field should be visible")
    public void the_password_field_should_be_visible() {
        Assert.assertTrue(Hooks.driver.findElement(By.name("password")).isDisplayed());
    }

    @Then("the login button should be visible")
    public void the_login_button_should_be_visible() {
        Assert.assertTrue(Hooks.driver.findElement(By.cssSelector("button[type='submit']")).isDisplayed());
    }
}
