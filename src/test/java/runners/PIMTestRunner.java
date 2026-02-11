package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefinitions"},
        tags = "@PIM",
        plugin = {
                "pretty",
                "html:target/cucumber-pim-report.html",
                "json:target/cucumber-pim-report.json"
        }
)
public class PIMTestRunner extends AbstractTestNGCucumberTests {
}
