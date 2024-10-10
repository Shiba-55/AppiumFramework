package com.ce.libraies;

import com.epam.healenium.SelfHealingDriver;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.Scenario;
import lombok.Data;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;

@Data
public class TestContext {
    private WebDriver driver = null;
    private AppiumDriver mobDriver = null;
    private PageObjectManager pageObjectManager = null;
    private Scenario scenario = null;
    private String testCaseID = null;
    private String moduleName = null;
    private String screenshotFolderName = null;
    private String captureScreenshot = null;
    private String appType = null;
    private HashMap<String, String> mapTestData = null;
    private String environment = null;
    private String currentTestUserName = null;
    private String browser = null;
    private String VM_Name = null;
    private WebDriverWait wait = null;
}