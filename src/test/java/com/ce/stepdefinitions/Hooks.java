package com.ce.stepdefinitions;

import com.ce.libraies.PageObjectManager;
import com.ce.libraies.FLUtilities;
import com.ce.libraies.TestContext;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.*;
import io.qameta.allure.Allure;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.*;

@Slf4j
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
        try {
            if (testContext.getMobDriver() == null) {
                String[] command1 = {"cmd.exe", "/C", "Start", "D:\\project\\Appium\\src\\test\\resources\\AppiumStart.bat"};
                Process p1 = Runtime.getRuntime().exec(command1);
                System.out.println("Appium Started");
                Thread.sleep(20000);
                String[] command = {"cmd.exe", "/C", "Start", "D:\\project\\Appium\\src\\test\\resources\\Emulator.bat"};
                Process p = Runtime.getRuntime().exec(command);
                Thread.sleep(22000);
                System.out.println("Emulator Started");

            }
        } catch (Exception e) {
            log.info(e.getMessage());
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        if (testContext.getMobDriver() == null) {
            testContext.setMobDriver(getMobDriver(testContext));
        }
      //  testContext.setPageObjectManager(new PageObjectManager(testContext.getDriver()));
        testContext.setScenario(scenario);
    }

    @After
    public void cleanUp() throws Exception {
      try {
          closeBrowser(testContext);
          System.out.println("Closed");
      } finally {
          WindowsProcessKiller pKiller = new WindowsProcessKiller();

          // To kill a command prompt
          String processName = "cmd.exe";
          boolean isRunning = pKiller.isProcessRunning(processName);

          System.out.println("is " + processName + " running : " + isRunning);

          if (isRunning) {
              WindowsProcessKiller.killProcess(processName);
              System.out.println("All Command Prompt windows have been attempted to be closed.");

          } else {
              System.out.println("Not able to find the process : " + processName);
          }
      }

    }

    @AfterStep
    public void ss() {
        if (testContext.getScenario().isFailed())
            Allure.addAttachment("Any text", new ByteArrayInputStream(((TakesScreenshot) testContext.getMobDriver()).getScreenshotAs(OutputType.BYTES)));

    }
}