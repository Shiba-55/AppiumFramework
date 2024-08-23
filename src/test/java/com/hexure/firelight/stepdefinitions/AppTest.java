package com.hexure.firelight.stepdefinitions;

import com.hexure.firelight.libraies.FLUtilities;
import com.hexure.firelight.libraies.TestContext;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;

import org.junit.Assert;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AppTest extends FLUtilities {
    static  AppiumDriver driver;
     TestContext testContext;
    public static void main(String[] args) throws InterruptedException {
      //  openMobileApp();
        new AppTest().openMobileApp();
    }

    public void openMobileApp() {
        DesiredCapabilities cap = getDesiredCapabilities();
        URL url = null;
       try{
           url = new URL("http://127.0.0.1:4723/");
       } catch (MalformedURLException m) {
            m.printStackTrace();
       }
       // assert url != null;
        driver = new AppiumDriver(url, cap);
        testContext.setMobDriver(driver);
            System.out.println("driver not null");


     //   testContext.setPageObjectManager(new PageObjectManager(driver));
       driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
      // Thread.sleep(5000);
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70));
//      //  driver.manage().deleteAllCookies();
//        try {wait.until(ExpectedConditions.alertIsPresent());}
//        catch (Exception e) {
//            System.out.println("Alert not present");
//            e.printStackTrace();
//        }
//       driver.switchTo().alert().accept();
 //      AndroidDriver aa = (AndroidDriver) driver;
//       aa.pressKey(KeyEvent);
//        ((AndroidDriver) driver).pressKeyCode
//                        (AndroidKeyCode.BACK);
        System.out.println("Application Started");
     //   Thread.sleep(10000);
//        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(AppiumBy.id("com.ubercab:id/welcome_screen_continue")));
//        driver.findElement(AppiumBy.id("com.ubercab:id/welcome_screen_continue")).click();
//      if (!driver.findElements(AppiumBy.xpath("//android.widget.Button[@resource-id=\"com.ubercab:id/welcome_screen_continue\"]")).isEmpty()) {
//          driver.findElement(AppiumBy.xpath("//android.widget.Button[@resource-id=\"com.ubercab:id/welcome_screen_continue\"]")).click();
//      }
//      //  Thread.sleep(10000);
//      //  driver.switchTo().alert().accept();
//        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(AppiumBy.id("com.google.android.gms:id/cancel")));
//        driver.findElement(AppiumBy.id("com.google.android.gms:id/cancel")).click();
//      //  Thread.sleep(20000);
//      //  System.out.println(driver.getTitle());
//
//        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(AppiumBy.xpath("//android.widget.EditText")));
//        Assert.assertTrue("Phone number field not displayed", driver.findElement(AppiumBy.xpath("//android.widget.EditText")).isDisplayed());
//        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(AppiumBy.xpath("//android.widget.Button[@text=\"Continue with Google\"]")));
//        Assert.assertTrue("Continue field not displayed", driver.findElement(AppiumBy.xpath("//android.widget.Button[@text=\"Continue with Google\"]")).isDisplayed());
//        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(AppiumBy.xpath("//android.widget.Button[@text=\"Envelope Continue with Email\"]")));
//        Assert.assertTrue("Continue with Google field not displayed", driver.findElement(AppiumBy.xpath("//android.widget.Button[@text=\"Envelope Continue with Email\"]")).isDisplayed());
//       // softAssert.assertTrue(driver.findElement(AppiumBy.xpath("//android.widget.Button[@resource-id=\"apple-login-btn\"]")).isDisplayed(),"Continue with Apple field not displayed");
//        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(AppiumBy.xpath("//android.widget.Button[@resource-id=\"continue-with-email-btn\"]")));
//        Assert.assertTrue("Continue with Email field not displayed", driver.findElement(AppiumBy.xpath("//android.widget.Button[@resource-id=\"continue-with-email-btn\"]")).isDisplayed());
//        System.out.println("Task Complete");

    }

    private static DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities cap = new DesiredCapabilities();
       // cap.setCapability("deviceName","Shiba Shankar");
         cap.setCapability("deviceName","sdk gphone64_x86_64");
        //cap.setCapability("udid","GYOVVS7DBIQ4WSEY");
          cap.setCapability("udid","emulator-5554");
        cap.setCapability("platformName","Android");
        //cap.setCapability("platformVersion","14");
        cap.setCapability("platformVersion","15");
        cap.setCapability("automationName","uiAutomator2");
        cap.setCapability("adbExecTimeout",50000);
        cap.setCapability("appPackage","com.ubercab");
        cap.setCapability("appActivity","com.ubercab.presidio.app.core.root.RootActivity");
        return cap;
    }
    private static DesiredCapabilities getDesiredCapabilities1() {
        DesiredCapabilities cap = new DesiredCapabilities();
         cap.setCapability("deviceName","Shiba Shankar");
        //cap.setCapability("deviceName","sdk gphone64_x86_64");
        cap.setCapability("udid","GYOVVS7DBIQ4WSEY");
        //cap.setCapability("udid","emulator-5554");
        cap.setCapability("platformName","Android");
        cap.setCapability("platformVersion","14");
        //cap.setCapability("platformVersion","15");
        cap.setCapability("automationName","uiAutomator2");
        cap.setCapability("adbExecTimeout",50000);
        cap.setCapability("appPackage","com.ubercab");
        cap.setCapability("appActivity","com.ubercab.presidio.app.core.root.RootActivity");
        return cap;
    }
}
