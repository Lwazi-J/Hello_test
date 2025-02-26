@api @hello
Feature: Hello API
  As an API consumer
  I want to manage hello messages
  So that I can interact with the Hello service

  Background:
    Given the API is running at "http://localhost:8080"
    And I set the Content-Type header to "application/json"
    And I set the Accept header to "application/json"

  @get
  Scenario: Get default hello message
    When I send a GET request to "/hello"
    Then the response status code should be 200
    And the response body should be a string

  @get @path-param
  Scenario: Get personalized hello message
    When I send a GET request to "/hello/John"
    Then the response status code should be 200
    And the response body should be a string

  @post @create
  Scenario: Create a new hello message
    Given I set the request body to:
      """
      {
        "name": "Alice",
        "message": "Hello, Alice"
      }
      """
    When I send a POST request to "/hello"
    Then the response status code should be 200
    And the response content type should include "application/json"
    And the response body should be a JSON object with the following properties:
      | property | value         |
      | name     | Alice         |
      | message  | Hello, Alice |

  @put @update
  Scenario: Update an existing hello message
    Given I set the request body to:
      """
      {
        "name": "Bob",
        "message": "Hello, Bob! How are you today?"
      }
      """
    When I send a PUT request to "/hello/name/Bob"
    Then the response status code should be 200
    And the response content type should include "application/json"
    And the response body should be a JSON object with the following properties:
      | property | value                        |
      | name     | Bob                          |
      | message  | Hello, Bob! How are you today? |

  @delete
  Scenario: Delete a hello message
    When I send a DELETE request to "/hello/name/Charlie"
    Then the response status code should be 200

  @get @collection
  Scenario: Get all hello messages
    When I send a GET request to "/hello/all"
    Then the response status code should be 200
    And the response content type should include "application/json"
    And the response body should be a JSON array

  @edge-cases
  Scenario Outline: Test various names with GET requests
    When I send a GET request to "/hello/<name>"
    Then the response status code should be 200
    And the response body should be a string

    Examples:
      | name          |
      | John          |
      | Jane-Doe      |
      | user_123      |
      | специальный   |