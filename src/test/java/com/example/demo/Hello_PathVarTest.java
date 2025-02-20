package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Hello_PathVarTest {

    private HelloController helloController;

    @BeforeEach
    void setUp() {
        helloController = new HelloController();
    }

    /**
     * Test the hello endpoint with name parameter returns personalized greeting
     */
    @Test
    void testHelloWithName() {
        // Given
        String name = "John";
        String expectedResponse = "Hello John";

        // When
        String actualResponse = helloController.helloWithName(name);

        // Then
        assertEquals(expectedResponse, actualResponse, "Hello method should return personalized greeting with name");
    }

    /**
     * Test hello with name endpoint handles special characters
     */
    @Test
    void testHelloWithNameSpecialCharacters() {
        // Given
        String name = "O'Connor-Smith";
        String expectedResponse = "Hello O'Connor-Smith";

        // When
        String actualResponse = helloController.helloWithName(name);

        // Then
        assertEquals(expectedResponse, actualResponse, "Hello method should handle names with special characters");
    }

}
