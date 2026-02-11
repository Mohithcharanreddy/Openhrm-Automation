package stepdefinitions;

import base.DriverSetup;
import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.ForgotPasswordPage;

public class ForgotPasswordSteps {

    private ForgotPasswordPage forgotPasswordPage;

    @When("I click on the forgot password link")
    public void i_click_on_the_forgot_password_link() {
        forgotPasswordPage = new ForgotPasswordPage(DriverSetup.getDriver());
        forgotPasswordPage.clickForgotPassword();
    }

    @Then("I should be navigated to the reset password page")
    public void i_should_be_navigated_to_the_reset_password_page() {
        forgotPasswordPage = new ForgotPasswordPage(DriverSetup.getDriver());
        Assert.assertTrue(forgotPasswordPage.isForgotPasswordPageDisplayed(), "Reset Password page is not displayed!");
    }

    @Given("I am on the forgot password page")
    public void i_am_on_the_forgot_password_page() {
        forgotPasswordPage = new ForgotPasswordPage(DriverSetup.getDriver());
        forgotPasswordPage.clickForgotPassword();
        Assert.assertTrue(forgotPasswordPage.isForgotPasswordPageDisplayed(), "Reset Password page is not displayed!");
    }

    @When("I enter valid username {string}")
    public void i_enter_valid_username(String username) {
        forgotPasswordPage = new ForgotPasswordPage(DriverSetup.getDriver());
        forgotPasswordPage.enterUsername(username);
    }

    @When("I enter invalid username {string}")
    public void i_enter_invalid_username(String username) {
        forgotPasswordPage = new ForgotPasswordPage(DriverSetup.getDriver());
        forgotPasswordPage.enterUsername(username);
    }

    @When("I click on the reset button")
    public void i_click_on_the_reset_button() {
        forgotPasswordPage = new ForgotPasswordPage(DriverSetup.getDriver());
        forgotPasswordPage.clickResetPassword();
    }

    @Then("I should see a success message {string}")
    public void i_should_see_a_success_message(String expected) {
        forgotPasswordPage = new ForgotPasswordPage(DriverSetup.getDriver());
        String actual = forgotPasswordPage.getResetSuccessMessage();
        Assert.assertTrue(actual != null && !actual.trim().isEmpty(), "Reset success message not displayed!");
        Assert.assertTrue(actual.toLowerCase().contains(expected.toLowerCase()),
                "Expected message to contain: " + expected + " but got: " + actual);
    }
}
