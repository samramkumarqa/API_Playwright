package com.qa.api.tests;

import java.io.IOException;
import java.util.HashMap;
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

public class CreateUserTestWithJsonStringTest {
	Playwright playwright;
	APIRequest request;
	APIRequestContext requestContext;
	
	static String emailId;
	
	@BeforeTest
	public void setup() {
		playwright = Playwright.create();
		request = playwright.request();
		requestContext = request.newContext();
	}
	
	@AfterTest
	public void tearDown() {
		playwright.close();
	}
	
	public static String getRandomEmail() {
		emailId = "testpwautomation"+ System.currentTimeMillis()+"@gmail.com";
		System.out.println("Email id : "+emailId);
		return emailId;
	}
	
	@Test
	public void createUserTest() throws IOException {
		
		//String json:
		String reqJsonBody = "{\n"
				+ "    \"name\": \"tom1\",\n"
				+ "    \"email\": \"NCfmwhNHqG38@gmail.com\",\n"
				+ "    \"gender\": \"female\",\n"
				+ "    \"status\": \"inactive\"\n"
				+ "}";
		
		
		//POST Call: create a user
		APIResponse apiPostResponse = requestContext.post("https://gorest.co.in/public/v2/users", 
				RequestOptions.create()
					.setHeader("Content-Type", "application/json")
					.setHeader("Authorization", "Bearer b0d22025200b28f8f689282d38896494eccec09288a7155deb1ca8b3dc78b99e")
					.setData(reqJsonBody));
		
		System.out.println(apiPostResponse.status());
		Assert.assertEquals(apiPostResponse.status(), 201);
		Assert.assertEquals(apiPostResponse.statusText(), "Created");
		
		System.out.println(apiPostResponse.text());
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode postJsonResponse = objectMapper.readTree(apiPostResponse.body());
		System.out.println(postJsonResponse.toPrettyString());
		
		//Capture id from the post json response
		String userid = postJsonResponse.get("id").asText();
		System.out.println("user id : "+userid);
		
		//GET Call: Fetch the same user id
		
		System.out.println();
		
		APIResponse apiGetResponse = requestContext.get("https://gorest.co.in/public/v2/users/"+userid, 
				RequestOptions.create()
					.setHeader("Authorization", "Bearer b0d22025200b28f8f689282d38896494eccec09288a7155deb1ca8b3dc78b99e"));
		
			Assert.assertEquals(apiGetResponse.status(), 200);
			Assert.assertEquals(apiGetResponse.statusText(), "OK");	
			
			System.out.println(apiGetResponse.text());
			
			Assert.assertTrue(apiGetResponse.text().contains(userid));
			Assert.assertTrue(apiGetResponse.text().contains("ramtesting"));
			//Assert.assertTrue(apiGetResponse.text().contains(emailId));
	}
}
