package com.example.demo.service;

import com.example.demo.Greeting;
import com.example.demo.GreetingDao;
import com.example.demo.GreetingRequest;
import com.example.demo.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GreetingServiceImpl implements GreetingService {

    private final GreetingDao greetingDao;

    @Autowired
    public GreetingServiceImpl(GreetingDao greetingDao) {
        this.greetingDao = greetingDao;
    }

    @Override
    public List<Greeting> getAllGreetings() {
        return greetingDao.findAll();
    }

    @Override
    public Optional<Greeting> getGreetingById(String id) {
        if (!StringUtils.hasText(id)) {
            return Optional.empty();
        }
        return greetingDao.findById(id);
    }

    @Override
    public Greeting createGreeting(GreetingRequest request) {
        // Sanitize input
        String sanitizedName = sanitizeInput(request.getName());
        String sanitizedMessage = sanitizeInput(request.getMessage());
        
        // Create new greeting with sanitized input
        Greeting greeting = new Greeting(
                UUID.randomUUID().toString(),
                sanitizedMessage,
                sanitizedName
        );
        
        return greetingDao.save(greeting);
    }

    @Override
    public Optional<Greeting> updateGreeting(String id, GreetingRequest request) {
        if (!StringUtils.hasText(id)) {
            return Optional.empty();
        }
        
        Optional<Greeting> existingGreeting = greetingDao.findById(id);
        if (existingGreeting.isEmpty()) {
            return Optional.empty();
        }
        
        // Sanitize input
        String sanitizedName = sanitizeInput(request.getName());
        String sanitizedMessage = sanitizeInput(request.getMessage());
        
        // Create updated greeting with sanitized input
        Greeting updatedGreeting = new Greeting(
                id,
                sanitizedMessage,
                sanitizedName
        );
        
        return greetingDao.update(id, updatedGreeting);
    }

    @Override
    public boolean deleteGreeting(String id) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
        return greetingDao.delete(id);
    }
    
    // Helper method to sanitize input
    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        
        // Remove potentially dangerous characters
        return input.replaceAll("[<>\"'&]", "");
    }
}