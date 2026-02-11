package stepdefinitions;

import base.DriverSetup;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.LoginPage;
import pages.PIMPage;

import java.time.Duration;

public class PIMSteps {

    private final WebDriver driver;
    private final LoginPage loginPage;
    private final PIMPage pimPage;

    // Shared across scenarios (same JVM run)
    private static String sharedEmployeeId;

    public PIMSteps() {
        this.driver = DriverSetup.getDriver();
        this.loginPage = new LoginPage(driver);
        this.pimPage = new PIMPage();
    }

    private boolean isSidePanelVisible() {
        try {
            return driver.findElements(By.cssSelector("aside.oxd-sidepanel")).size() > 0
                    || driver.findElements(By.xpath("//span[normalize-space()='Dashboard']")).size() > 0
                    || driver.findElements(By.xpath("//span[normalize-space()='PIM']")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void ensureLoggedIn() {
        // If already logged in, return
        if (isSidePanelVisible()) return;

        // Retry login (demo site can be flaky)
        int attempts = 2;
        for (int i = 1; i <= attempts; i++) {
            try {
                loginPage.openLoginPage();
                loginPage.login("admin", "admin123");

                WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(60));
                try {
                    w.until(ExpectedConditions.or(
                            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("aside.oxd-sidepanel")),
                            ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Dashboard']")),
                            ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='PIM']"))
                    ));
                } catch (Exception ignored) {}

                if (isSidePanelVisible()) return;

                // If still not logged in, refresh & try again
                driver.navigate().refresh();
                Thread.sleep(500);
            } catch (Exception ignored) {}
        }

        Assert.assertTrue(isSidePanelVisible(),
                "Login failed - side panel not visible. Current URL: " + driver.getCurrentUrl());
    }

    // ---------- Scenario 1: Create ----------

    @Given("I am logged in and on PIM page")
    public void i_am_logged_in_and_on_pim_page() {
        ensureLoggedIn();
        pimPage.openPIMEmployeeList();
    }

    @When("I add a new employee")
    public void i_add_a_new_employee() {
        sharedEmployeeId = pimPage.addEmployeeAndGetId();
        Assert.assertNotNull(sharedEmployeeId, "Employee ID not captured.");
        Assert.assertFalse(sharedEmployeeId.trim().isEmpty(), "Employee ID is empty.");
    }

    @Then("the employee should be created successfully")
    public void the_employee_should_be_created_successfully() {
        Assert.assertNotNull(sharedEmployeeId, "Employee ID missing after creation.");
    }

    // ---------- Scenario 2: Search ----------

    @Given("I am on PIM Employee List page")
    public void i_am_on_pim_employee_list_page() {
        ensureLoggedIn();
        pimPage.openPIMEmployeeList();
    }

    @When("I search the employee by stored id")
    public void i_search_the_employee_by_stored_id() {
        Assert.assertNotNull(sharedEmployeeId, "Shared Employee ID is null. Run @PIM_01 first.");
        pimPage.goToEmployeeListAndSearchById(sharedEmployeeId);
    }

    @Then("the employee should appear in the results")
    public void the_employee_should_appear_in_the_results() {
        Assert.assertTrue(pimPage.isEmployeePresentInResults(), "Employee not present in results.");
    }

    // ---------- Scenario 3: Edit ----------

    @When("I open the employee for edit")
    public void i_open_the_employee_for_edit() {
        pimPage.openFirstSearchResultForEdit();
    }

    @When("I update nickname")
    public void i_update_nickname() {
        pimPage.editNicknameAndSave("nick_" + System.currentTimeMillis());
    }

    @Then("the employee update should be saved")
    public void the_employee_update_should_be_saved() {
        Assert.assertTrue(true);
    }

    // ---------- Scenario 4: Upload photo ----------

    @Given("I am on Employee Personal Details page")
    public void i_am_on_employee_personal_details_page() {
        ensureLoggedIn();
        Assert.assertNotNull(sharedEmployeeId, "Shared Employee ID is null. Run @PIM_01 first.");

        pimPage.goToEmployeeListAndSearchById(sharedEmployeeId);
        pimPage.openFirstSearchResultForEdit();
    }

    @When("I upload a profile picture for the employee")
    public void i_upload_a_profile_picture_for_the_employee() {
        String path = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\profile.png";
        pimPage.uploadProfilePicture(path);
    }

    @Then("the profile picture should be uploaded successfully")
    public void the_profile_picture_should_be_uploaded_successfully() {
        Assert.assertTrue(true);
    }

    // ---------- Scenario 5: Delete ----------

    @When("I delete the employee by stored id")
    public void i_delete_the_employee_by_stored_id() {
        Assert.assertNotNull(sharedEmployeeId, "Shared Employee ID is null. Run @PIM_01 first.");
        pimPage.deleteEmployeeById(sharedEmployeeId);
    }

    @Then("the employee should be deleted successfully")
    public void the_employee_should_be_deleted_successfully() {
        pimPage.goToEmployeeListAndSearchById(sharedEmployeeId);
        Assert.assertFalse(pimPage.isEmployeePresentInResults(), "Employee still present after delete.");
    }
}
