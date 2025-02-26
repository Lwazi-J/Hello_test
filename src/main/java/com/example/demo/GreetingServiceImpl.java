package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GreetingServiceImpl implements GreetingService{

    private final List<Greeting> greetings = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(GreetingServiceImpl.class);

    @Override
    public List<Greeting> getAllGreetings() {
        log.info("Retrieving all greetings");
        return new ArrayList<>(greetings);
    }

    @Override
    public Optional<Greeting> getGreetingById(String id) {
        log.info("Retrieving greeting with id: {}", id);
        return greetings.stream().filter(g -> g.getId().equals(id)).findFirst();
    }

    @Override
    public Greeting createGreeting(GreetingRequest request) {
        validateRequest(request);

        String id = String.valueOf(greetings.size() + 1);
        Greeting greeting = new Greeting(id, String.format("Hello %s! Your message: %s", request.getName(), request.getMessage()));

        log.info("Creating new greeting with id: {}", id);
        greetings.add(greeting);
        return greeting;
    }

    @Override
    public Optional<Greeting> updateGreeting(String id, GreetingRequest request) {
        validateRequest(request);

        Optional<Greeting> existingGreeting = getGreetingById(id);
        if (existingGreeting.isEmpty()) {
            log.warn("Attempted to update non-existent greeting with id: {}", id);
            return Optional.empty();
        }

        int index = greetings.indexOf(existingGreeting.get());
        Greeting updatedGreeting = new Greeting(id, String.format("Hello %s! Your updated message: %s", request.getName(), request.getMessage()));

        log.info("Updating greeting with id: {}", id);
        greetings.set(index, updatedGreeting);
        return Optional.of(updatedGreeting);
    }

    @Override
    public boolean deleteGreeting(String id) {
        Optional<Greeting> greeting = getGreetingById(id);
        if (greeting.isEmpty()) {
            log.warn("Attempted to delete non-existent greeting with id: {}", id);
            return false;
        }

        log.info("Deleting greeting with id: {}", id);
        return greetings.remove(greeting.get());
    }

    private void validateRequest(GreetingRequest request) {
        if (request == null) {
            log.error("Request is null");
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            log.error("Name is null or empty");
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            log.error("Message is null or empty");
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
    }
}
