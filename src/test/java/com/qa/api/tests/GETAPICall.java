package com.qa.api.tests;

import java.io.IOException;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;


public class GETAPICall {
	
	Playwright playwright;
	APIRequest request;
	APIRequestContext requestContext;
	
	@BeforeTest
	public void setup() {
		playwright = Playwright.create();
		request = playwright.request();
		requestContext = request.newContext();
	}
	
	@Test
	public void getSpecificUserApiTest() {
		APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users",
				RequestOptions.create()
					.setQueryParam("id", 7527454)
					.setQueryParam("status", "active")
				);
		int statusCode = apiResponse.status();
		System.out.println("Response status code is : "+statusCode);
		Assert.assertEquals(statusCode,200);
		Assert.assertEquals(apiResponse.ok(), true);
		
		String statusResText = apiResponse.statusText();
		System.out.println(statusResText);
		
		System.out.println("---print api response with plain text---");
		System.out.println("----------------------------------------");
		System.out.println(apiResponse.text());
		System.out.println("----------------------------------------");
	}
	
	@Test
	public void getUsersApiTest() throws IOException {
		APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");
		
		int statusCode = apiResponse.status();
		System.out.println("Response status code is : "+statusCode);
		Assert.assertEquals(statusCode,200);
		Assert.assertEquals(apiResponse.ok(), true);
		
		String statusResText = apiResponse.statusText();
		System.out.println(statusResText);
		
		System.out.println("---print api response with plain text---");
		System.out.println(apiResponse.text());
		
		System.out.println(apiResponse.body());
		
		System.out.println("---print api json response---");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonResponse = objectMapper.readTree(apiResponse.body());
		String jsonPrettyResponse = jsonResponse.toPrettyString();
		System.out.println(jsonPrettyResponse);
		
		System.out.println("---print api url---");
		System.out.println(apiResponse.url());
		
		System.out.println("---print api response headers---");
		Map<String, String> headersMap = apiResponse.headers();
		System.out.println(headersMap);
		Assert.assertEquals(headersMap.get("content-type"), "application/json; charset=utf-8");
	}

	@AfterTest
	public void tearDown() {
		playwright.close();
	}
}
