package com.hexure.firelight.stepdefinitions;

import com.hexure.firelight.libraies.FLException;
import com.hexure.firelight.libraies.FLUtilities;
import com.hexure.firelight.libraies.TestContext;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.io.ByteArrayInputStream;

public class CommonStepdefinition extends FLUtilities {

    private static final Logger Log = LogManager.getLogger(CommonStepdefinition.class);
    private AppiumDriver driver;
    private TestContext testContext;

    public CommonStepdefinition(TestContext context) {
        testContext = context;
        driver = context.getMobDriver();
    }

    @Given("Open page {string}")
    public void userIsOnFLLoginPage(String url) {
        configProperties.setProperty("QA.url", url);
        System.out.println("url == " + url);
        openLoginPage(driver, testContext);
    }

    @Then("Verify Page Title is {string}")
    public void verifyPageTitleIs(String title) {
        Assert.assertEquals("Page title verification failed!", title, driver.getTitle().trim());
    }

    @Then("User Enters value {string} in  {string} {string} having {string} {string}")
    public void enterValueInTextboxHaving(String valueToSend, String wizardType, String fieldName, String locator, String attributeValue) {
        sendKeys(driver, elementByLocator(driver, locator, null, null, attributeValue), valueToSend);
    }

    @Then("User Enters value from JSON {string} in  {string} {string} having {string} {string}")
    public void enterValueInTextboxJSON(String valueToSend, String wizardType, String fieldName, String locator, String attributeValue) {
        sendKeys(driver, elementByLocator(driver, locator, null, null, attributeValue), testContext.getMapTestData().get(valueToSend));
    }

    @Then("User Clicks element {string} {string} having {string} {string}")
    public void clickButtonHaving(String fieldName, String wizardType, String locator, String attributeValue) {
        clickElement(driver, elementByLocator(driver, locator, null, null, attributeValue));
    }

    @Then("User Verifies Default Value {string} having {string} {string} is {string}")
    public void verifyValue(String wizardType, String locator, String attributeValue, String value) {
        Assert.assertTrue(value.equalsIgnoreCase(verifyValue(driver, elementByLocator(driver, locator, null, null, attributeValue))));
    }

    @Then("User Selects value {string} for {string} {string} having {string} {string}")
    public void selectValueDropDownHaving(String option, String fieldName, String wizardType, String locator, String attributeValue) {
        new Select(elementByLocator(driver, locator, null, null, attributeValue)).selectByVisibleText(option);
    }

    @Given("User is on login page for TestCase {string}")
    public void userIsOnLoginPage(String testCaseID) {
       // commonSetup(testCaseID);
       // getMobDriver(testContext);
//        try {getExplicitWait(testContext.getMobDriver(), testContext).until(ExpectedConditions.alertIsPresent());
//            driver.switchTo().alert().accept();
//            System.out.println("Alert present");
//        }
//        catch (Exception e) {
//            System.out.println("Alert not present");
//            e.printStackTrace();
//            throw new FLException("Alert not present >> "+e.getMessage());
//        }
    }

    private void commonSetup(String testCaseID) {
        testContext.setTestCaseID(testCaseID);
        testContext.setScreenshotFolderName(testCaseID);
        System.out.println("Environment = " + testContext.getEnvironment());
        System.out.println("ApplicationType = " + testContext.getAppType());
        System.out.println("TestCaseID = " + testContext.getTestCaseID());
        System.out.println("CaptureScreenshot = " + testContext.getCaptureScreenshot());
        System.out.println("ScreenshotFolder = " + testContext.getScreenshotFolderName());
        testContext.setMapTestData(getTestData(testCaseID, testContext));
        Log.info("TEST CASE {} STARTED", testCaseID);
    }

    @Given("I am on the login screen")
    public void i_am_on_the_login_screen() {
        // Write code here that turns the phrase above into concrete actions
        captureScreenshot(driver,testContext,true);
        System.out.println("SSM Successful");

    }

    @Then("User Verifies element {string} {string} having {string} {string}")
    public void userVerifiesElementHaving(String fieldName, String wizardType, String locator, String attributeValue) {
        Assert.assertTrue(fieldName + " was not displayed", elementByLocator(driver, locator, null, null, attributeValue).isDisplayed());
    }

    @Then("User Verifies text of {string} {string} Should be {string} having {string} {string}")
    public void userVerifiesTextOfShouldBeHaving(String fieldName, String wizardType, String expectedText, String locator, String attributeValue) {
        Assert.assertEquals("Text did not match", expectedText, elementByLocator(driver,locator,null,null,attributeValue).getText().trim());
    }

}
