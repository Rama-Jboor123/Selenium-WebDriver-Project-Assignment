package com.ramajboor.selenium.runners;

import com.ramajboor.selenium.utils.ConfigReader;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.xml.XmlSuite;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.ramajboor.selenium",
        plugin = {
                "pretty",
                "html:target/cucumber-report/cucumber.html",
                "json:target/cucumber-report/cucumber.json"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @BeforeClass(alwaysRun = true)
    public void setUpClass(ITestContext context) {
        XmlSuite suite = context.getCurrentXmlTest().getSuite();
        if (suite.getDataProviderThreadCount() == XmlSuite.DEFAULT_DATA_PROVIDER_THREAD_COUNT) {
            suite.setDataProviderThreadCount(Integer.parseInt(ConfigReader.get("parallelThreads", "3")));
        }
        super.setUpClass(context);
    }

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
