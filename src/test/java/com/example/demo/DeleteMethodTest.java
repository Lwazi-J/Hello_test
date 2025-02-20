package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteMethodTest {

    private HelloController helloController;

    @BeforeEach
    void setUp() {
        helloController = new HelloController();
    }

    /**
     * Helper method to create a sample greeting
     */
    private Greeting createSampleGreeting(String name, String message) {
        GreetingRequest request = new GreetingRequest(name, message);
        return helloController.createGreeting(request);
    }


    /**
     * Test DELETE endpoint successfully removes existing greeting
     */
    @Test
    void testDeleteGreeting() {
        // Given
        Greeting greeting = createSampleGreeting("John", "Test message");

        // When
        ResponseEntity<Void> deleteResponse = helloController.deleteGreeting(greeting.getId());

        // Then
        assertTrue(deleteResponse.getStatusCode().is2xxSuccessful(), "Delete should return successful status");

        List<Greeting> remainingGreetings = helloController.getAllGreetings();
        assertTrue(remainingGreetings.isEmpty(), "Greeting list should be empty after deletion");
    }

    /**
     * Test DELETE endpoint returns not found for non-existent greeting
     */
    @Test
    void testDeleteNonExistentGreeting() {
        // Given
        String nonExistentId = "999";

        // When
        ResponseEntity<Void> response = helloController.deleteGreeting(nonExistentId);

        // Then
        assertTrue(response.getStatusCode().is4xxClientError(), "Should return 404 for non-existent greeting");
    }
}
