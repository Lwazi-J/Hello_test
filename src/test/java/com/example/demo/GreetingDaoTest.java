package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class GreetingDaoTest {

    private GreetingDao greetingDao;

    @BeforeEach
    void setUp() {
        greetingDao = new GreetingDaoImpl();
    }

    @Test
    @DisplayName("Test save greeting")
    void testSave() {
        // Given
        Greeting greeting = new Greeting(null, "Test message", "Test Name");

        // When
        Greeting saved = greetingDao.save(greeting);

        // Then
        assertNotNull(saved);
        assertEquals("1", saved.getId());
        assertEquals(greeting.getMessage(), saved.getMessage());
        assertEquals(greeting.getName(), saved.getName());
    }

    @Test
    @DisplayName("Test find all greetings")
    void testFindAll() {
        // Given
        greetingDao.save(new Greeting(null, "First message", "First Name"));
        greetingDao.save(new Greeting(null, "Second message", "Second Name"));

        // When
        List<Greeting> results = greetingDao.findAll();

        // Then
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(g -> g.getMessage().equals("First message")));
        assertTrue(results.stream().anyMatch(g -> g.getMessage().equals("Second message")));
        assertTrue(results.stream().anyMatch(g -> g.getName().equals("First Name")));
        assertTrue(results.stream().anyMatch(g -> g.getName().equals("Second Name")));
    }

    @Test
    @DisplayName("Test find greeting by ID")
    void testFindById() {
        // Given
        Greeting saved = greetingDao.save(new Greeting(null, "Test message", "Test Name"));

        // When
        Optional<Greeting> found = greetingDao.findById(saved.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals(saved.getMessage(), found.get().getMessage());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    @DisplayName("Test find non-existent greeting")
    void testFindById_NotFound() {
        // When
        Optional<Greeting> result = greetingDao.findById("999");

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test update greeting")
    void testUpdate() {
        // Given
        Greeting original = greetingDao.save(new Greeting(null, "Original message", "Original Name"));
        Greeting updated = new Greeting(original.getId(), "Updated message", "Updated Name");

        // When
        Optional<Greeting> result = greetingDao.update(original.getId(), updated);

        // Then
        assertTrue(result.isPresent());
        assertEquals(original.getId(), result.get().getId());
        assertEquals("Updated message", result.get().getMessage());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    @DisplayName("Test update non-existent greeting")
    void testUpdate_NotFound() {
        // Given
        Greeting updated = new Greeting("999", "Updated message", "Updated Name");

        // When
        Optional<Greeting> result = greetingDao.update("999", updated);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test delete greeting")
    void testDelete() {
        // Given
        Greeting saved = greetingDao.save(new Greeting(null, "Test message", "Test Name"));

        // When
        boolean deleted = greetingDao.delete(saved.getId());

        // Then
        assertTrue(deleted);
        assertTrue(greetingDao.findAll().isEmpty());
    }

    @Test
    @DisplayName("Test delete non-existent greeting")
    void testDelete_NotFound() {
        // When
        boolean result = greetingDao.delete("999");

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Test exists")
    void testExists() {
        // Given
        Greeting saved = greetingDao.save(new Greeting(null, "Test message", "Test Name"));

        // When & Then
        assertTrue(greetingDao.exists(saved.getId()));
        assertFalse(greetingDao.exists("999"));
    }

    @Test
    @DisplayName("Test sequential ID generation")
    void testSequentialIdGeneration() {
        // When
        Greeting first = greetingDao.save(new Greeting(null, "First", "First Name"));
        Greeting second = greetingDao.save(new Greeting(null, "Second", "Second Name"));
        Greeting third = greetingDao.save(new Greeting(null, "Third", "Third Name"));

        // Then
        assertEquals("1", first.getId());
        assertEquals("2", second.getId());
        assertEquals("3", third.getId());
    }
}