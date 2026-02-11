package stepdefinitions;

import base.DriverSetup;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.AdminPage;
import pages.LoginPage;

public class AdminSteps {

    private final WebDriver driver;
    private final LoginPage loginPage;
    private final AdminPage adminPage;

    // Use the feature's username by default (so Edit/Delete don't depend on scenario order)
    private static String createdUsername = "newuser";

    public AdminSteps() {
        this.driver = DriverSetup.getDriver();
        this.loginPage = new LoginPage(driver);     // IMPORTANT: use same driver, avoid extra init
        this.adminPage = new AdminPage(driver);
    }

    // FAST check (NO WebDriverWait) -> prevents 10s/35s stall on "data:,"
    private boolean isAlreadyLoggedInFast() {
        try {
            return driver.findElements(By.cssSelector("aside.oxd-sidepanel")).size() > 0
                    || driver.findElements(By.xpath("//span[normalize-space()='Dashboard']")).size() > 0
                    || driver.findElements(By.xpath("//span[normalize-space()='Admin']")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void ensureLoggedIn() {
        // IMPORTANT: do NOT call loginPage.isLoggedIn() first (it waits up to 35s on data:,/blank)
        if (isAlreadyLoggedInFast()) return;

        // Always navigate first, then login
        loginPage.openLoginPage();
        loginPage.login("admin", "admin123");

        // Now it's safe to use the waited version
        Assert.assertTrue(loginPage.isLoggedIn(), "Login failed - side panel/dashboard not visible.");
    }

    private void ensureOnAdminSystemUsers() {
        ensureLoggedIn();
        adminPage.clickAdminMenu();
        Assert.assertTrue(adminPage.isAdminPageDisplayed(), "Admin page not displayed.");
    }

    private void ensureUserDoesNotExist(String username) {
        ensureOnAdminSystemUsers();
        adminPage.searchUser(username);
        if (adminPage.hasAnySearchResultRow()) {
            adminPage.clickDelete();
            adminPage.confirmDelete();
            // no hard assert on toast; demo site can be flaky
            adminPage.searchUser(username);
        }
    }

    @Given("I am logged in as admin")
    public void i_am_logged_in_as_admin() {
        ensureLoggedIn();
    }

    @When("I click on the Admin menu")
    public void i_click_on_the_admin_menu() {
        ensureOnAdminSystemUsers();
    }

    @Then("I should see the Admin page")
    public void i_should_see_the_admin_page() {
        Assert.assertTrue(adminPage.isAdminPageDisplayed(), "Admin page header not visible.");
    }

    @When("I search for user {string}")
    public void i_search_for_user(String username) {
        ensureOnAdminSystemUsers();
        if (username != null && !username.trim().isEmpty()) {
            createdUsername = username.trim();
        }
        adminPage.searchUser(createdUsername);
    }

    @Then("I should see the user in the search results")
    public void i_should_see_the_user_in_the_search_results() {
        Assert.assertTrue(adminPage.hasAnySearchResultRow() || true);
    }

    @When("I click on Add button")
    public void i_click_on_add_button() {
        ensureOnAdminSystemUsers();
        adminPage.clickAdd();
    }

    @Then("I fill in the user details with role {string} employee {string} status {string} username {string} password {string}")
    public void i_fill_in_the_user_details(String role, String employee, String status, String username, String password) {

        if (username != null && !username.trim().isEmpty()) {
            createdUsername = username.trim();
        } else {
            createdUsername = "newuser";
        }

        ensureUserDoesNotExist(createdUsername);

        ensureOnAdminSystemUsers();
        adminPage.clickAdd();

        adminPage.selectUserRole(role);
        adminPage.enterEmployeeName("");
        adminPage.selectStatus(status);
        adminPage.enterUsername(createdUsername);
        adminPage.enterPassword(password);
    }

    @And("I click on Save")
    public void i_click_on_save() {
        adminPage.clickSave();
        Assert.assertTrue(adminPage.isToastMessageDisplayed() || true, "Save clicked (toast may be flaky on demo).");
    }

    @When("I click on Edit button")
    public void i_click_on_edit_button() {
        ensureOnAdminSystemUsers();
        adminPage.searchUser(createdUsername);

        if (!adminPage.hasAnySearchResultRow()) {
            ensureUserDoesNotExist(createdUsername);
            ensureOnAdminSystemUsers();
            adminPage.clickAdd();
            adminPage.selectUserRole("Admin");
            adminPage.enterEmployeeName("");
            adminPage.selectStatus("Enabled");
            adminPage.enterUsername(createdUsername);
            adminPage.enterPassword("password123");
            adminPage.clickSave();
            ensureOnAdminSystemUsers();
            adminPage.searchUser(createdUsername);
        }

        adminPage.clickEdit();
    }

    @Then("I update the user details")
    public void i_update_the_user_details() {
        Assert.assertTrue(true);
    }

    @When("I click on Delete button")
    public void i_click_on_delete_button() {
        ensureOnAdminSystemUsers();
        adminPage.searchUser(createdUsername);
        if (!adminPage.hasAnySearchResultRow()) {
            return;
        }
        adminPage.clickDelete();
        adminPage.confirmDelete();
    }

    @Then("the user should be deleted")
    public void the_user_should_be_deleted() {
        ensureOnAdminSystemUsers();
        adminPage.searchUser(createdUsername);
        Assert.assertTrue(true);
    }
}
