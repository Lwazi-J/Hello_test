package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PostMethodTest {

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
     * Test POST endpoint creates greeting with name and message
     */
    @Test
    void testCreateGreeting() {
        // Given
        String name = "John";
        String message = "Good morning!";
        GreetingRequest request = new GreetingRequest(name, message);

        // When
        Greeting response = helloController.createGreeting(request);

        // Then
        assertNotNull(response, "Response should not be null");
        assertEquals("1", response.getId(), "First greeting should have ID 1");
        assertEquals(name, response.getName(), "Greeting should contain the name");
        assertTrue(response.getMessage().contains(message), "Greeting should contain the message");
    }

    /**
     * Test POST endpoint creates sequential IDs
     */
    @Test
    void testCreateGreetingSequentialIds() {
        // When
        Greeting first = createSampleGreeting("John", "First");
        Greeting second = createSampleGreeting("Jane", "Second");
        Greeting third = createSampleGreeting("Bob", "Third");

        // Then
        assertEquals("1", first.getId(), "First greeting should have ID 1");
        assertEquals("2", second.getId(), "Second greeting should have ID 2");
        assertEquals("3", third.getId(), "Third greeting should have ID 3");
    }
}
