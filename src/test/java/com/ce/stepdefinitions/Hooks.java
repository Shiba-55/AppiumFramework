package com.hexure.firelight.stepdefinitions;

import com.hexure.firelight.libraies.PageObjectManager;
import com.hexure.firelight.libraies.FLUtilities;
import com.hexure.firelight.libraies.TestContext;
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
//           ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "upsert.bat");
//           File dir = new File("D:\\project\\Appium\\src\\test\\resources\\Emulator.bat");
//           pb.directory(dir);
//           Process p = pb.start();

                String[] command1 = {"cmd.exe", "/C", "Start", "D:\\project\\Appium\\src\\test\\resources\\AppiumStart.bat"};
                Process p1 = Runtime.getRuntime().exec(command1);
                Thread.sleep(10000);
                System.out.println("Appium Started");
                String[] command = {"cmd.exe", "/C", "Start", "D:\\project\\Appium\\src\\test\\resources\\Emulator.bat"};
                Process p = Runtime.getRuntime().exec(command);
                Thread.sleep(4000);
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
        testContext.setPageObjectManager(new PageObjectManager(testContext.getDriver()));
        testContext.setScenario(scenario);
    }

    @After(order = 0)
    public void cleanUp() throws Exception {
        closeBrowser(testContext);
        System.out.println("Closed");
//        try {
//          //  Runtime.getRuntime().exec("taskkill /f /im cmd.exe") ;
//          //  Runtime.getRuntime().exec("command.exe  /C" + "exit");
//            String[] command = {"cmd.exe", "/C", "Start", "D:\\project\\Appium\\src\\test\\resources\\AllureReport.bat"};
//            Process p = Runtime.getRuntime().exec(command);
//            System.out.println(" CMD Closed");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception(e);
//        }

//        try {
//            // Command to list all cmd.exe processes
//            String commandList = "wmic process where \"name='cmd.exe'\" get ProcessId";
//
//            // Execute the command to get process IDs
//            Process listProcess = Runtime.getRuntime().exec(commandList);
//
//            // Read the output
//            BufferedReader reader = new BufferedReader(new InputStreamReader(listProcess.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                line = line.trim();
//                if (line.matches("\\d+")) {
//                    // Skip the first line (header) and get process IDs
//                    String commandKill = "taskkill /F /PID " + line;
//                    Runtime.getRuntime().exec(commandKill);
//                }
//            }
//
//            // Wait for all commands to complete
//            listProcess.waitFor();
//
//            System.out.println("All Command Prompt windows have been attempted to be closed.");
//        } catch (IOException e) {
//            System.err.println("IOException occurred: " + e.getMessage());
//        } catch (InterruptedException e) {
//            System.err.println("InterruptedException occurred: " + e.getMessage());
//        }

        WindowsProcessKiller pKiller = new WindowsProcessKiller();

        // To kill a command prompt
        String processName = "cmd.exe";
        boolean isRunning = pKiller.isProcessRunning(processName);

        System.out.println("is " + processName + " running : " + isRunning);

        if (isRunning) {
            WindowsProcessKiller.killProcess(processName);
            System.out.println("All Command Prompt windows have been attempted to be closed.");

        }

        else {
            System.out.println("Not able to find the process : "+processName);
        }


    }

    @AfterStep
    public void ss() {
        if (testContext.getScenario().isFailed())
            Allure.addAttachment("Any text", new ByteArrayInputStream(((TakesScreenshot) testContext.getMobDriver()).getScreenshotAs(OutputType.BYTES)));

    }
}