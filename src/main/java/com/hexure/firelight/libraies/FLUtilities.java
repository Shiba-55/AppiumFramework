package com.hexure.firelight.libraies;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FLUtilities extends BaseClass {
    private static final Logger Log = LogManager.getLogger(FLUtilities.class);
    private TestContext testContext;
    protected void syncElement(WebDriver driver, WebElement element, String conditionForWait) {
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .pollingEvery(Duration.ofMillis(200))
                    .withTimeout(Duration.ofSeconds(Integer.parseInt(configProperties.getProperty("explicit_wait"))))
                    .ignoring(NoSuchElementException.class);
            switch (conditionForWait) {
                case "ToVisible":
                    wait.until((WebDriver d) -> ExpectedConditions.visibilityOf(element).apply(d));
                    break;
                case "ToClickable":
                    wait.until((WebDriver d) -> ExpectedConditions.elementToBeClickable(element).apply(d));
                    break;
                case "ToInvisible":
                    wait.until((WebDriver d) -> ExpectedConditions.invisibilityOf(element).apply(d));
                    break;
                default:
                    throw new FLException("Invalid Condition " + conditionForWait);
            }
        } catch (StaleElementReferenceException | NoSuchElementException e) {
            System.out.println("No Such Element Exception is showing on searching element " + element);
        } catch (Exception e) {
            Log.error("Could Not Sync WebElement ", e);
            throw new FLException("Could Not Sync WebElement " + e.getMessage());
        }
    }

    protected void clickElement(WebDriver driver, WebElement element) {
        syncElement(driver, element, EnumsCommon.TOVISIBLE.getText());
        try {
            scrollToWebElement(driver, element);
            element.click();
        } catch (Exception e) {
            try {
                Log.info("Retrying click using Actions class");
                new Actions(driver).moveToElement(element).click().perform();
            } catch (Exception ex) {
                Log.warn("Retrying click using moveByOffset due to failure", ex);
                try {
                    new Actions(driver).moveToElement(element).moveByOffset(10, 10).click().perform();
                } catch (Exception finalEx) {
                    Log.error("Could not click WebElement using Actions and moveByOffset", finalEx);
                    throw new FLException("Could not click WebElement using Actions and moveByOffset: " + finalEx.getMessage() + "Element -> " + element);
                }
            }
        }
    }

    protected void scrollToWebElement(WebDriver driver, WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            Log.error("Could Not Scroll WebElement ", e);
            throw new FLException("Could Not Scroll WebElement " + e.getMessage() + element);
        }
    }

    protected void clickElementByJSE(WebDriver driver, WebElement element) {
        waitForPageToLoad(driver);
        syncElement(driver, element, EnumsCommon.TOCLICKABLE.getText());
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            Log.error("Clicking WebElement By JavaScriptExecutor Failed ", e);
            throw new FLException("Clicking WebElement By JavaScriptExecutor Failed " + e.getMessage() + element);
        }
    }


    protected void sendKeys(WebDriver driver, WebElement element, String stringToInput) {
        waitForPageToLoad(driver);
        syncElement(driver, element, EnumsCommon.TOVISIBLE.getText());
        try {
            element.clear();
            clickElement(driver, element);
            element.sendKeys(stringToInput);
            element.sendKeys(Keys.TAB);
        } catch (Exception e) {
            try {
                waitForPageToLoad(driver);
                syncElement(driver, element, EnumsCommon.TOCLICKABLE.getText());
                // Scroll the element into view
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                element.clear();
                // Move to the element using Actions class
                new Actions(driver).moveToElement(element).sendKeys(stringToInput).perform();
                element.sendKeys(Keys.TAB);
            } catch (Exception e1) {
                Log.error("SendKeys Failed ", e1);
                throw new FLException(stringToInput + " could not be entered in element" + e1.getMessage());
            }
        }
        waitForPageToLoad(driver);
    }

    protected String verifyValue(WebDriver driver, WebElement element) {
        waitForPageToLoad(driver);
        syncElement(driver, element, EnumsCommon.TOVISIBLE.getText());
        try {
            element.clear();
            clickElement(driver, element);
            return element.getAttribute("value");
        } catch (Exception e) {
            try {
                waitForPageToLoad(driver);
                return element.getText();
            } catch (Exception e1) {
                Log.error("SendKeys Failed ", e1);
                throw new FLException("No value fetched from element" + e1.getMessage());
            }
        }
    }

    protected void sleepInMilliSeconds(int milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            Log.error("Explicit Sleep Failed ", e);
            throw new FLException("Explicit Sleep Failed " + e.getMessage());
        }
    }

    protected void waitUntilDropDownListPopulated(WebDriver driver, Select dropdown) {
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .pollingEvery(Duration.ofMillis(250))
                    .withTimeout(Duration.ofSeconds(15))
                    .ignoring(NoSuchElementException.class);

            wait.until(driver1 -> dropdown.getOptions().size() > 3);
        } catch (StaleElementReferenceException se) {
            throw new FLException("Element is not available in DOM " + se.getMessage());
        } catch (Exception e) {
            throw new FLException("Error in populating dropdown " + e.getMessage());
        }
    }

    protected void clickElement(WebDriver driver, String stringXpath) {
        WebElement element = driver.findElement(By.xpath(stringXpath));
        syncElement(driver, element, EnumsCommon.TOCLICKABLE.getText());
        try {
            new Actions(driver).moveToElement(element).click().perform();
        } catch (Exception e) {
            Log.error("Could Not Click WebElement ", e);
            throw new FLException("Could Not Click WebElement " + e.getMessage());
        }
    }

    public WebElement findElement(WebDriver driver, String stringXpath) {
        try {
            syncElement(driver, driver.findElement(By.xpath(stringXpath)), EnumsCommon.TOVISIBLE.getText());
            return driver.findElement(By.xpath(stringXpath));
        } catch (NoSuchElementException e) {
            System.out.println("No Such Element Exception is showing on searching element " + stringXpath);
            return null;
        }
    }

    public WebElement findElementByID(WebDriver driver, By id) {
        try {
            syncElement(driver, driver.findElement(id), EnumsCommon.TOVISIBLE.getText());
            return driver.findElement(id);
        } catch (NoSuchElementException e) {
            System.out.println("No Such Element Exception is showing on searching element having id " + id);
            return null;
        }
    }

    public List<WebElement> findElements(WebDriver driver, String stringXpath) {
        return driver.findElements(By.xpath(stringXpath));
    }

    public List<WebElement> findElementsByID(WebDriver driver, By id) {
        return driver.findElements(id);
    }

    // Method to select or deselect a checkbox based on the user's action
    protected void checkBoxSelectYesNO(String userAction, WebElement element) {
        // Check if the user action requires the checkbox to be selected
        if (getCheckBoxAction(userAction)) {
            // If the checkbox is not already selected, click it to select
            if (element.getAttribute("aria-checked").equals("false"))
                element.click();
        } else {
            // If the user action requires the checkbox to be deselected
            // If the checkbox is already selected, click it to deselect
            if (element.getAttribute("aria-checked").equals("true"))
                element.click();
        }
    }

    protected boolean verifyCheckBoxSelectYesNO(String userAction, WebElement element) {
        // Perform the action of selecting/deselecting the checkbox
        checkBoxSelectYesNO(userAction, element);

        // Determine the expected state based on user action
        boolean expectedState = getCheckBoxAction(userAction);

        // Get the actual state from the element's attribute
        boolean actualState = element.getAttribute("aria-checked").equals("true");

        // Compare the expected state with the actual state
        return expectedState == actualState;
    }

    private boolean getCheckBoxAction(String action) {
        // Check if the action string matches any of the strings indicating a selection action (case-insensitive)
        return action.equalsIgnoreCase("yes") || action.equalsIgnoreCase("check") || action.equalsIgnoreCase("checked") || action.equalsIgnoreCase("selected");
    }


    /**
     * index of a given column in Excel
     *
     * @param headerRow  - Header row of an Excel sheet
     * @param columnName - Column name of header row
     * @return int, index of that Column
     */
    public static int findColumnIndex(Row headerRow, String columnName) {
        Iterator<Cell> cellIterator = headerRow.cellIterator();

        // Iterate through each cell in the header row
        while (cellIterator.hasNext()) {
            // Get the current cell
            Cell cell = cellIterator.next();

            // Check if the current cell's value matches the column name (case-insensitive)
            if (columnName.equalsIgnoreCase(getCellColumnValue(cell))) {
                // Return the index of the cell if the column name matches
                return cell.getColumnIndex();
            }
        }
        return -1; // Column not found
    }

    public static String getCellColumnValue(Cell cell) {
        // If the cell is null, return an empty string; otherwise, return the trimmed string value of the cell
        return cell == null ? "" : cell.toString().trim();
    }

    protected List<WebElement> getElements(WebDriver driver, By locator) {
        try
        {
            waitForPageToLoad(driver);
            return driver.findElements(locator);
        }
        catch(Exception e)
        {
            Log.error("Could not find elements with locator " + locator, e);
            throw new FLException("Could not find elements with locator >>>> " + e.getMessage());
        }
    }

    /**
     * By using Action class and its methods along with X/Y co-ordinates this method will draw/add digital signature on
     * specified WebElement (passed as parameter).
     *
     * @param driver The WebDriver instance
     * @param element The WebElement on which digital signature needs to be added
     */
    protected void addDigitalSignature(WebDriver driver, WebElement element) {
        try {
            Actions builder = new Actions(driver);
            scrollToWebElement(driver, element);
            builder.moveToElement(element).perform();
            builder.clickAndHold(element).perform();
            builder.moveByOffset(0, -90).perform();
            builder.moveToElement(element).perform();
            builder.clickAndHold(element).perform();
            builder.moveByOffset(0, 75).perform();
            builder.moveToElement(element).perform();
            builder.clickAndHold(element).perform();
            builder.moveByOffset(150, 0).perform();
            builder.moveToElement(element).perform();
            builder.clickAndHold(element).perform();
            builder.moveByOffset(-150, 0).perform();
            builder.moveToElement(element).perform();

            builder.moveToElement(element).perform();
            builder.clickAndHold(element).perform();
            builder.moveByOffset(0, -90).perform();
            builder.moveToElement(element).perform();
            builder.clickAndHold(element).perform();
            builder.moveByOffset(0, 75).perform();
            builder.moveToElement(element).perform();
            builder.clickAndHold(element).perform();
            builder.moveByOffset(150, 0).perform();
            builder.moveToElement(element).perform();
            builder.clickAndHold(element).perform();
            builder.moveByOffset(-150, 0).perform();
            builder.moveToElement(element).perform();
        } catch (Exception e) {
            Log.error("Adding Digital Signature Failed", e);
            throw new FLException("Adding Digital Signature Failed " + e.getMessage());
        }
    }

    protected WebElement elementByLocator( WebDriver driver, String locatorType, String tagName, String attribute, String attributeValue) {
        WebElement element = null;
        WebDriverWait appiumWait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(configProperties.getProperty("explicit_wait"))));
        switch (locatorType.trim().toLowerCase()){
            case "id" :
                appiumWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(AppiumBy.id(attributeValue)));
                element = driver.findElement(By.id(attributeValue));
                break;
            case "name" :
                appiumWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(AppiumBy.name(attributeValue)));
                element = driver.findElement(By.name(attributeValue));
                break;
            case "class" :
                appiumWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(AppiumBy.cssSelector("." + attributeValue.replaceAll(" ", "."))));
                element = driver.findElement(By.cssSelector("." + attributeValue.replaceAll(" ", ".")));
                break;
            case "xpath" :
                appiumWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(AppiumBy.xpath(attributeValue)));
                element = driver.findElement(By.xpath(attributeValue));
                break;
            default:
                new FLException("Invalid locator " + locatorType);
        }

        return element;
    }

    protected List<WebElement> elementsByLocator( WebDriver driver, String locatorType, String tagName, String attribute, String attributeValue) {
        List<WebElement> elements = null;
        switch (locatorType.trim().toLowerCase()){
            case "id" :
                elements = driver.findElements(AppiumBy.id(attributeValue));
                break;
            case "name" :
                elements = driver.findElements(AppiumBy.name(attributeValue));
                break;
            case "class" :
                elements = driver.findElements(AppiumBy.className(attributeValue));
                break;
            case "xpath" :
                elements = driver.findElements(AppiumBy.xpath(attributeValue));
                break;
            default:
                new FLException("Invalid locator " + locatorType);
        }

        return elements;
    }

    /**
     * Delete existing feature and runner files
     *
     * @param folderPath - Folder in which reports are saved
     */
    protected void deleteRunnerFeature(String folderPath) {
        File folder = new File(folderPath);

        try {
            Path directory = Paths.get(String.valueOf(folder));
            // Recursively delete the directory and its contents
            Files.walk(directory)
                    .sorted(Collections.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            System.out.println("Runner & Feature Folder deleted successfully.");
        } catch (IOException e) {
            System.err.println("Failed to delete the folder: " + e.getMessage());
        }
    }

    protected WebDriverWait getExplicitWait(AppiumDriver driver,TestContext testContext) {
     //   testContext.setWait(new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(configProperties.getProperty("implicit_wait")))));
        return testContext.getWait();
    }

}