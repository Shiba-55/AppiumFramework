package com.hexure.firelight.stepdefinitions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hexure.firelight.libraies.EnumsCommon;
import com.hexure.firelight.libraies.FLUtilities;
import com.hexure.firelight.libraies.TestContext;
import io.cucumber.java.en.Given;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static java.util.stream.Collectors.joining;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.DevToolsException;
import org.openqa.selenium.devtools.v126.network.Network;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.net.URLDecoder;
import java.util.*;
import java.io.*;

public class RecordActions extends FLUtilities {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private WebDriver driver;
    private TestContext testContext;

    Map<String, Map<String, String>> parentMap = new HashMap<>();
    List<Map<String, String>> a = new ArrayList<>();
    int stepsCount = 1;
    Map<Integer, Map<String, String>> stepsMap = new HashMap<>();
    Map<Integer, Map<String, String>> resultantMap = new HashMap<>();
    Map<String, Map<String, String>> elementLocatorMap = new HashMap<>();
    JavascriptExecutor executor = null;
    List<String> requestDataTypes = Arrays.asList("Stylesheet", "Script", "Image", "Other", "Font");
    List<String> ignoredRequest = Arrays.asList("ads", "google-analytics", "analytics.google");
    String url = "https://automationintesting.online/";

    public RecordActions(TestContext context) {
        testContext = context;
        driver = context.getDriver();
    }

    @Given("Record actions from the application")
    public void recordSteps() throws IOException, ParseException {
        Map<String, String> flowInterfaceMap = new HashMap<>();
        Map<String, Object> preferences = new HashMap<>();
        boolean flag = true;
        JSONObject jsonTestData = new JSONObject();
        JSONObject masterJson = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        String featureName = "Login functionality";
        String description = "Verify login functionality";
        String scenario = "Scenario1";
        String testCaseName = "Verify login functionality";
        String testCaseWorkbook = "Product1.xlsx";
        String testCaseWorkbook1 = "API.xlsx";
        String execute = "Yes";

        flowInterfaceMap.put("Feature Name", featureName);
        flowInterfaceMap.put("Description", description);
        flowInterfaceMap.put("Scenario", scenario);
        flowInterfaceMap.put("TestCaseName", testCaseName);
        flowInterfaceMap.put("TestCaseSheet", testCaseWorkbook);
        flowInterfaceMap.put("Execute", execute);
        appendRows(flowInterfaceMap);

        executor = (JavascriptExecutor) driver;
        deleteRunnerFeature(EnumsCommon.RESPONSE_FILES_PATH.getText() + scenario);
        AtomicInteger counter = new AtomicInteger(0);
        DevTools devTools = ((ChromiumDriver) driver).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), entry -> {
            if (URLDecoder.decode(entry.getRequest().getUrl()).contains(url) && !(requestDataTypes.stream().anyMatch(entry.getType().toString()::contains) || ignoredRequest.stream().anyMatch(URLDecoder.decode(entry.getRequest().getUrl())::contains))) {
                String postDataNL = entry.getRequest().getPostData().toString();
                Map<String, String> tempHeader = new HashMap<>();
                postDataNL = postDataNL.substring(postDataNL.indexOf("Optional") + 9).replaceAll("empty", "");
                Map<String, String> newResult = new HashMap<>();
                String responseBody = "";
                String responseFileName = "";
                int countRequest = counter.incrementAndGet();

                newResult.put("Steps", "Call API");
                newResult.put("BaseURI", URLDecoder.decode(entry.getRequest().getUrl()));
                newResult.put("EndPoint", "");
                newResult.put("Method", entry.getRequest().getMethod());
                newResult.put("Params", "");
                newResult.put("Auth", "");
                for (String key : entry.getRequest().getHeaders().keySet())
                    tempHeader.put(key, entry.getRequest().getHeaders().get(key).toString());

                String s = tempHeader.entrySet()
                        .stream()
                        .map(e -> e.getKey() + "=" + e.getValue())
                        .collect(joining(" | "));

                newResult.put("Headers", s);
                newResult.put("Body", postDataNL);
                newResult.put("Scripts", "");
                newResult.put("Field", "");
                newResult.put("Response", responseFileName);

                resultantMap.put(countRequest, newResult);

                try {
                    responseBody = new WebDriverWait(driver, Duration.ofSeconds(60))
                            .ignoring(DevToolsException.class)
                            .until(driver -> devTools.send(Network.getResponseBody(entry.getRequestId())).getBody());

                } catch (DevToolsException e) {
                    System.out.println("No response recieved for request ID " + entry.getRequestId());
                } finally {
                    if (!responseBody.equals("")) {
                        try {
                            responseFileName = EnumsCommon.RESPONSE_FILES_PATH.getText() + scenario + "\\API_Response" + countRequest + ".txt";
                            File responseFileInput = new File(responseFileName);
                            responseFileInput.getParentFile().mkdirs();
                            if (!responseFileInput.exists())
                                responseFileInput.createNewFile();
                            FileWriter responseFile = new FileWriter(responseFileName);
                            BufferedWriter writer = new BufferedWriter(responseFile);
                            writer.flush();
                            writer.write(responseBody);
                            writer.close();
                        } catch (IOException e) {
                            System.out.println("File handling issue");
                        }
                    }
                    newResult.put("Response", responseFileName);

                    if(resultantMap.containsKey(countRequest)) {
                        Map<String, String> tempMap = resultantMap.get(countRequest);
                        if (tempMap.containsKey("Response"))
                            tempMap.put("Response", responseFileName);
                        resultantMap.put(countRequest, tempMap);
                    }
                }
            }
        });

        driver.get(url);

        stepsMap.put(stepsCount++, stepsOpenPage(url));
        stepsMap.put(stepsCount++, stepsVerifyPage());
        BufferedReader bfn = new BufferedReader(new InputStreamReader(System.in));

        while (Boolean.parseBoolean(bfn.readLine())) {
            findElementAttribute();
        }

        File jsonFilePath = new File(EnumsCommon.ABSOLUTE_FILES_PATH.getText() + "elementAttributes.json");
        if (jsonFilePath.length() == 0)
            flag = false;

        JSONParser parser = new JSONParser();
        if (flag) {
            Object obj = parser.parse(new FileReader(EnumsCommon.ABSOLUTE_FILES_PATH.getText() + "elementAttributes.json"));
            jsonTestData = (JSONObject) obj;
            jsonTestData = (JSONObject) jsonTestData.get("testData");
        }

        for (String temp1 : parentMap.keySet()) {
            if (jsonTestData.containsKey(temp1)) {
                Map<String, String> tempClientData = (Map<String, String>) jsonTestData.get(temp1);
                for (String parentMapKey : parentMap.get(temp1).keySet()) {
                    if (tempClientData.containsKey(parentMapKey)) {
                        if (!tempClientData.get(parentMapKey).equalsIgnoreCase(parentMap.get(temp1).get(parentMapKey)))
                            tempClientData.put(parentMapKey, parentMap.get(temp1).get(parentMapKey));
                    } else {
                        tempClientData.put(parentMapKey, parentMap.get(temp1).get(parentMapKey));
                    }
                }
                masterJson.put(temp1, tempClientData);
            } else
                masterJson.put(temp1, parentMap.get(temp1));
        }

        for (Object temp1 : jsonTestData.keySet()) {
            String tempKey = (String) temp1;
            if (!parentMap.containsKey(tempKey))
                masterJson.put(tempKey, jsonTestData.get(tempKey));
        }

        jsonObject.put("testData", masterJson);
        FileWriter jsonTestData1 = new FileWriter(EnumsCommon.ABSOLUTE_FILES_PATH.getText() + "elementAttributes.json");
        BufferedWriter writer = new BufferedWriter(jsonTestData1);
        writer.write(gson.toJson(jsonObject));
        writer.close();
        createTestCaseSheet(testCaseWorkbook, scenario);
        createAPITestCaseSheet(testCaseWorkbook1, scenario);
    }

    public void findElementAttribute() {
        String xPath = "";
        String xPath1 = "";
        String ariaLabel = "";
        Map<String, String> tempMap = new HashMap<>();
        Map<String, String> tempLocatorMap = new HashMap<>();
        String pageTitle = "";
        List<String> attributeList = Arrays.asList("id", "name");
        String tagName = String.valueOf(executor.executeScript("var elem = document.activeElement; return elem.tagName"));
            a = (ArrayList) executor.executeScript("return document.activeElement.attributes");
        try {
            ariaLabel = (String) executor.executeScript("return document.activeElement.ariaLabel");
            pageTitle = driver.getTitle();
            for (Map<String, String> a1 : a) {
                tempMap.put(a1.get("name"), a1.get("value"));
                xPath = "//" + tagName.toLowerCase() + "[@" + a1.get("name") + "=\"" + a1.get("value") + "\"]";
                if (driver.findElements(By.xpath(xPath)).size() == 1 && xPath1.equals("")) {
                    if (attributeList.contains(a1.get("name")))
                        tempLocatorMap.put(a1.get("name"), a1.get("value"));
                    else
                        tempLocatorMap.put("xPath", xPath);
                    xPath1 = xPath;
                }
            }
            parentMap.put(tagName.toLowerCase() + "_" + pageTitle + "_" + ariaLabel.replaceAll(" ", ""), tempMap);
        } catch (NullPointerException e) {
            ariaLabel = (String) executor.executeScript("return document.activeElement.value");
            parentMap.put(tagName.toLowerCase() + "_" + pageTitle + "_" + ariaLabel.replaceAll(" ", ""), tempMap);
        }
        elementLocatorMap.put(tagName.toLowerCase() + "_" + pageTitle + "_" + ariaLabel.replaceAll(" ", ""), tempLocatorMap);
        createSteps(tagName, pageTitle, ariaLabel, xPath1);
    }

    public void createSteps(String tagName, String pageTitle, String ariaLabel, String xPath1) {
        String key = tagName.toLowerCase() + "_" + pageTitle + "_" + ariaLabel.replaceAll(" ", "");
        Map<String, String> data = parentMap.get(key);
        String locatorType = elementLocatorMap.get(key).keySet().toString().replaceAll("[\\[\\]]", "");
        String locatorValue = elementLocatorMap.get(key).get(locatorType);
        String type = tagName;
        if (data.containsKey("type"))
            type = data.get("type");
        switch ((tagName + "_" + type).toLowerCase()) {
            case "input_text":
            case "input_password":
            case "textarea_textarea":
                stepsMap.put(stepsCount++, stepsDefaultValue(key, locatorType, locatorValue, type));
                stepsMap.put(stepsCount++, stepsEnterValue(key, locatorType, locatorValue, type));
                break;
            case "input_submit":
            case "button_button":
                stepsMap.put(stepsCount++, stepsClickElement(key, locatorType, locatorValue, type));
                driver.findElement(By.xpath(xPath1)).click();
                waitForPageToLoad(driver);
                stepsMap.put(stepsCount++, stepsVerifyPage());
                break;
        }
    }

    public Map<String, String> stepsDefaultValue(String key, String locatorType, String locatorValue, String type) {
        Map<String, String> stepsInterfaceMap = new HashMap<>();
        stepsInterfaceMap.put("Steps", "Verify Default Value");
        stepsInterfaceMap.put("Field Name", key);
        stepsInterfaceMap.put("Locator Type", locatorType);
        stepsInterfaceMap.put("Common Tag", locatorValue);
        stepsInterfaceMap.put("Wizard Control Types", type);
        stepsInterfaceMap.put("Test Data", String.valueOf(executor.executeScript("return document.activeElement.defaultValue")));
        return stepsInterfaceMap;
    }

    public Map<String, String> stepsEnterValue(String key, String locatorType, String locatorValue, String type) {
        Map<String, String> stepsInterfaceMap = new HashMap<>();
        stepsInterfaceMap.put("Steps", "Enter value");
        stepsInterfaceMap.put("Field Name", key);
        stepsInterfaceMap.put("Locator Type", locatorType);
        stepsInterfaceMap.put("Common Tag", locatorValue);
        stepsInterfaceMap.put("Wizard Control Types", type);
        stepsInterfaceMap.put("Test Data", String.valueOf(executor.executeScript("return document.activeElement.value")));
        return stepsInterfaceMap;
    }

    public Map<String, String> stepsClickElement(String key, String locatorType, String locatorValue, String type) {
        Map<String, String> stepsInterfaceMap = new HashMap<>();
        stepsInterfaceMap.put("Steps", "Click element");
        stepsInterfaceMap.put("Locator Type", locatorType);
        stepsInterfaceMap.put("Common Tag", locatorValue);
        stepsInterfaceMap.put("Wizard Control Types", type);
        return stepsInterfaceMap;
    }

    public Map<String, String> stepsVerifyPage() {
        Map<String, String> stepsInterfaceMap = new HashMap<>();
        stepsInterfaceMap.put("Steps", "Verify Page Title");
        stepsInterfaceMap.put("Test Data", (String) executor.executeScript("return document.title"));
        return stepsInterfaceMap;
    }

    public Map<String, String> stepsOpenPage(String url) {
        Map<String, String> stepsInterfaceMap = new HashMap<>();
        stepsInterfaceMap.put("Steps", "Open page");
        stepsInterfaceMap.put("Test Data", url);
        return stepsInterfaceMap;
    }

    public void appendRows(Map<String, String> flowInterfaceMap) {
        try {
            File file = new File(EnumsCommon.ABSOLUTE_FILES_PATH.getText() + "FlowInterface.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();
            int countRow = sheet.getLastRowNum() + 1;
            Row headerRow = iterator.next().getSheet().getRow(0);
            Row row = sheet.createRow(countRow);
            int countCell = 0;
            boolean flag = true;
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                if (sheet.getRow(rowNum).getCell(findColumnIndex(headerRow, "Scenario")).toString().equalsIgnoreCase(flowInterfaceMap.get("Scenario"))) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                for (String tags : flowInterfaceMap.keySet())
                    row.createCell(findColumnIndex(headerRow, tags)).setCellValue(flowInterfaceMap.get(tags));
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeSheet(String sheetName, XSSFWorkbook book, File file) throws IOException {
        for (int i = book.getNumberOfSheets() - 1; i >= 0; i--) {
            Sheet tmpSheet = book.getSheetAt(i);
            if (tmpSheet.getSheetName().equals(sheetName)) {
                book.removeSheetAt(i);
            }
        }
    }

    public void createTestCaseSheet(String testCaseWorkbook, String scenario) {
        try {
            File file = new File(EnumsCommon.ABSOLUTE_CLIENTFILES_PATH.getText() + testCaseWorkbook);
            XSSFWorkbook workbook = null;
            if (!file.exists()) {
                file.createNewFile();
                workbook = new XSSFWorkbook();
            } else {
                workbook = new XSSFWorkbook(new FileInputStream(file));
                removeSheet(scenario, workbook, file);
            }
            Sheet sheet = workbook.createSheet(scenario);
            int countRow = 0;

            Row row = sheet.createRow(countRow);
            Iterator<Row> iterator = sheet.iterator();
            row.createCell(countRow++).setCellValue("Steps");
            row.createCell(countRow++).setCellValue("Field Name");
            row.createCell(countRow++).setCellValue("Locator Type");
            row.createCell(countRow++).setCellValue("Attribute");
            row.createCell(countRow++).setCellValue("Common Tag");
            row.createCell(countRow++).setCellValue("Wizard Control Types");
            row.createCell(countRow++).setCellValue("Test Data");
            row.createCell(countRow++).setCellValue("File Name");
            row.createCell(countRow).setCellValue("Steps Range");

            Row headerRow = iterator.next().getSheet().getRow(0);
            for (Integer count : stepsMap.keySet()) {
                row = sheet.createRow(count);
                for (String tags : stepsMap.get(count).keySet())
                    row.createCell(findColumnIndex(headerRow, tags)).setCellValue(stepsMap.get(count).get(tags));
            }

            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAPITestCaseSheet(String testCaseWorkbook, String scenario) {
        try {
            File file = new File(EnumsCommon.ABSOLUTE_CLIENTFILES_PATH.getText() + testCaseWorkbook);
            XSSFWorkbook workbook = null;
            if (!file.exists()) {
                file.createNewFile();
                workbook = new XSSFWorkbook();
            } else {
                workbook = new XSSFWorkbook(new FileInputStream(file));
                removeSheet(scenario, workbook, file);
            }
            Sheet sheet = workbook.createSheet(scenario);
            int countRow = 0;

            Row row = sheet.createRow(countRow);
            Iterator<Row> iterator = sheet.iterator();
            row.createCell(countRow++).setCellValue("Steps");
            row.createCell(countRow++).setCellValue("BaseURI");
            row.createCell(countRow++).setCellValue("EndPoint");
            row.createCell(countRow++).setCellValue("Method");
            row.createCell(countRow++).setCellValue("Params");
            row.createCell(countRow++).setCellValue("Auth");
            row.createCell(countRow++).setCellValue("Headers");
            row.createCell(countRow++).setCellValue("Body");
            row.createCell(countRow++).setCellValue("Scripts");
            row.createCell(countRow++).setCellValue("Response");
            row.createCell(countRow).setCellValue("Field");

            Row headerRow = iterator.next().getSheet().getRow(0);
            for (Integer count : resultantMap.keySet()) {
                row = sheet.createRow(count);
                for (String tags : resultantMap.get(count).keySet())
                    row.createCell(findColumnIndex(headerRow, tags)).setCellValue(resultantMap.get(count).get(tags));
            }

            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
