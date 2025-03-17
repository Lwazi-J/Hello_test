package com.example.demo.helloapitest.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class HelloApiStepDefs {

    private String apiBaseUrl;
    private RequestSpecification request;
    private Response response;
    private String requestBody;

    @BeforeEach
    public void setup() {
        // Set a default base URI if needed, or leave it to be set by the scenario
        RestAssured.baseURI = "http://localhost:8080";  // Default, can be overridden
        RestAssured.reset();
        // Initialize request with default values
        request = given().contentType("application/json");
    }

    @Given("the API is running at {string}")
    public void theAPIIsRunningAt(String url) {
        apiBaseUrl = url;
        // Update the base request with the new URL if needed
        request = given().contentType("application/json");
    }

    @Given("I set the {word} header to {string}")
    public void iSetTheHeaderTo(String headerName, String headerValue) {
        request = request.header(headerName, headerValue);
    }

    @Given("I set the request body to:")
    public void iSetTheRequestBodyTo(String docString) {
        requestBody = docString;
        request = request.body(docString);
    }

    @When("I send a GET request to {string}")
    public void iSendAGETRequestTo(String endpoint) {
        String fullUrl = apiBaseUrl + endpoint;
        response = request.get(fullUrl);
        logRequestResponse(fullUrl, "GET");
    }

    @When("I send a POST request to {string}")
    public void iSendAPOSTRequestTo(String endpoint) {
        String fullUrl = apiBaseUrl + endpoint;
        response = request.post(fullUrl);
        logRequestResponse(fullUrl, "POST");
    }

    @When("I send a PUT request to {string}")
    public void iSendAPUTRequestTo(String endpoint) {
        String fullUrl = apiBaseUrl + endpoint;
        response = request.put(fullUrl);
        logRequestResponse(fullUrl, "PUT");
    }

    @When("I send a DELETE request to {string}")
    public void iSendADELETERequestTo(String endpoint) {
        String fullUrl = apiBaseUrl + endpoint;
        response = request.delete(fullUrl);
        logRequestResponse(fullUrl, "DELETE");
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.getStatusCode(),
                "Expected status code " + expectedStatusCode + " but got " + response.getStatusCode());
    }

    @Then("the response body should be a string")
    public void theResponseBodyShouldBeAString() {
        // Verify the response is not null and is a string
        String responseBody = response.asString();
        assertNotNull(responseBody, "Response body should not be null");

        // Additional check to verify it's not JSON (optional)
        try {
            new JSONObject(responseBody);
            fail("Response body is a JSON object, not a plain string");
        } catch (JSONException e1) {
            try {
                new JSONArray(responseBody);
                fail("Response body is a JSON array, not a plain string");
            } catch (JSONException e2) {
                // If both fail, it's likely a plain string - success
            }
        }
    }

    @Then("the response content type should include {string}")
    public void theResponseContentTypeShouldInclude(String contentType) {
        String actualContentType = response.getContentType();
        assertTrue(actualContentType != null && actualContentType.contains(contentType),
                "Expected content type to include '" + contentType +
                        "' but was '" + actualContentType + "'");
    }

    @Then("the response body should be a JSON object with the following properties:")
    public void theResponseBodyShouldBeAJSONObjectWithTheFollowingProperties(List<Map<String, String>> dataTable) {
        String responseBody = response.asString();
        assertNotNull(responseBody, "Response body should not be null");

        try {
            JSONObject jsonObject = new JSONObject(responseBody);

            for (Map<String, String> row : dataTable) {
                String property = row.get("property");
                String expectedValue = row.get("value");

                assertTrue(jsonObject.has(property),
                        "JSON object missing expected property: " + property +
                                ". Actual JSON: " + responseBody);

                assertEquals(expectedValue, jsonObject.getString(property),
                        "Property '" + property + "' has incorrect value. Expected: '" +
                                expectedValue + "', but was: '" + jsonObject.getString(property) + "'");
            }
        } catch (JSONException e) {
            fail("Failed to parse JSON object: " + e.getMessage() +
                    ". Response body: " + responseBody);
        }
    }

    @Then("the response body should be a JSON array")
    public void theResponseBodyShouldBeAJSONArray() {
        String responseBody = response.asString();
        assertNotNull(responseBody, "Response body should not be null");

        try {
            // This will throw an exception if the body is not a valid JSON array
            JSONArray jsonArray = new JSONArray(responseBody);
            assertTrue(jsonArray.length() >= 0, "Response should be a valid JSON array");
        } catch (JSONException e) {
            fail("Failed to parse JSON array: " + e.getMessage() +
                    ". Response body: " + responseBody);
        }
    }

    @Then("the response body should be a JSON array with at least {int} items")
    public void theResponseBodyShouldBeAJSONArrayWithAtLeastItems(int minItems) {
        String responseBody = response.asString();
        assertNotNull(responseBody, "Response body should not be null");

        try {
            JSONArray jsonArray = new JSONArray(responseBody);
            assertTrue(jsonArray.length() >= minItems,
                    "JSON array should have at least " + minItems + " items but had " +
                            jsonArray.length() + ". Array content: " + responseBody);
        } catch (JSONException e) {
            fail("Failed to parse JSON array: " + e.getMessage() +
                    ". Response body: " + responseBody);
        }
    }

    // Helper method to log request and response details for debugging
    private void logRequestResponse(String url, String method) {
        System.out.println("Request: " + method + " " + url);
        if (requestBody != null && !requestBody.isEmpty()) {
            System.out.println("Request Body: " + requestBody);
        }
        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asString());
    }
}