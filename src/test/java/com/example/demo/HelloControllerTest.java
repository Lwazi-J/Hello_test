package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HelloController
 */
public class HelloControllerTest {

    private HelloController helloController;

    @BeforeEach
    void setUp() {
        helloController = new HelloController();
    }

    /**
     * Test the hello endpoint returns the expected greeting
     */
    @Test
    void testHello() {
        // Given
        String expectedResponse = "Hello World";

        // When
        String actualResponse = helloController.hello();

        // Then
        assertEquals(expectedResponse, actualResponse, "Hello method should return 'Hello World'");
    }

    /**
     * Test hello endpoint returns non-null value
     */
    @Test
    void testHelloNotNull() {
        // When
        String response = helloController.hello();

        // Then
        assertNotNull(response, "Hello method should not return null");
    }
}