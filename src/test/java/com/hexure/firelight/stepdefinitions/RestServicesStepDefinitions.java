package com.hexure.firelight.stepdefinitions;

import java.util.List;
import java.util.Map;
import com.hexure.firelight.libraies.FLUtilities;
import com.hexure.firelight.libraies.RestAPICalls;
import com.hexure.firelight.libraies.TestContext;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import io.cucumber.java.en.Given;
import com.hexure.firelight.pages.RestMasterPage;
import org.junit.Assert;

public class RestServicesStepDefinitions extends FLUtilities {
	public RestMasterPage onRestMasterPage;
	Response response;
	public RestAPICalls rest_All;
	String respBody;
	int respStatus;
	String str_query = "";
	private final TestContext testContext;
	Map<String,String> map;

	public RestServicesStepDefinitions(TestContext context) {
		this.testContext = context;
		rest_All = new RestAPICalls();
		onRestMasterPage = context.getPageObjectManager().getRestMasterPage();
	}

	@Given("Call API {int} {string} request {string} URL with following parameters")
	public void hit_restPOST(int counterAPI, String restURL, String method, DataTable arg1) {
		map =  arg1.asMap(String.class,String.class);
		response = onRestMasterPage.callRESTservice(restURL,method,map, String.valueOf(counterAPI), testContext, rest_All);
		respBody = response.getBody().asString();
		respStatus = response.statusCode();
		System.out.println(respBody);
		System.out.println(respStatus);
	}

	@Given("Save Field from Response {string}")
	public void saveField(String parameters) {
		for (String key : parameters.split(", ")) {
			List<String> respFields = onRestMasterPage.saveResponseFields(response, key, testContext, rest_All);
			addPropertyValueInJSON(testContext.getTestCaseID(), testContext, respFields.get(0), respFields.get(1));
		}
	}


	@Given("Verify field from Response {string}")
	public void verifyResponse(String parameters) {
		for (String field : parameters.split(", ")) {
			List<String> respFields = onRestMasterPage.saveResponseFields(response, field, testContext, rest_All);
			Assert.assertEquals(respFields.get(1), testContext.getMapTestData().get(respFields.get(0)));
		}
	}

//
//
//	@SuppressWarnings("deprecation")
//	@Then("^Verify STATUS CODE matches \"(.*)\"$")
//	public void verifySTATUS_CODE(String statCode) throws Throwable {
//
//		int code = Integer.parseInt(statCode);
//		assertEquals(code,respStatus);
//
//	}
//
//	@Then("^Verify GUID matches with the GUID returned from \"(.*)\"$")
//	public void verifyPOST_DATA(String dbQuery, String dbRecord) throws Throwable {
//
//		assertEquals(respBody.toLowerCase().replace("\"",""),DEMORestMaster.getResponse(dbQuery, dbRecord).toLowerCase());
//
//	}


}

