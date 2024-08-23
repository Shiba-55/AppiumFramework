package com.hexure.firelight.stepdefinitions;

import com.hexure.firelight.libraies.PageObjectManager;
import com.hexure.firelight.libraies.FLUtilities;
import com.hexure.firelight.libraies.TestContext;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.qameta.allure.Allure;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

@EqualsAndHashCode(callSuper = true)
@Data
public class Hooks extends FLUtilities {
    private TestContext testContext;

    public Hooks(TestContext context) {
        testContext = context;
    }

    @Before
    public void setUp(Scenario scenario) {
        loadConfigData(testContext);

        if (testContext.getMobDriver() == null) {
            testContext.setMobDriver(getMobDriver(testContext));
        }
        testContext.setPageObjectManager(new PageObjectManager(testContext.getDriver()));
        testContext.setScenario(scenario);
    }

    @After
    public void cleanUp() {
        closeBrowser(testContext);
    }

    @AfterStep
    public void ss(){
        if(testContext.getScenario().isFailed())
            Allure.addAttachment("Any text", new ByteArrayInputStream(((TakesScreenshot) testContext.getMobDriver()).getScreenshotAs(OutputType.BYTES)));

    }
}