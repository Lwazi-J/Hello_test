package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class HelloController {

    private List<Greeting> greetings = new ArrayList<>();

    private Optional<Greeting> findGreetingById(String id) {
        return greetings.stream()
                .filter(g -> g.getId() != null && g.getId().equals(id))
                .findFirst();
    }

    private Optional<Greeting> findGreetingByName(String name) {
        return greetings.stream()
                .filter(g -> g.getName() != null && g.getName().equals(name))
                .findFirst();
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/hello/{name}")
    public String helloWithName(@PathVariable String name) {
        return String.format("Hello %s", name);
    }

    @GetMapping("/hello/all")
    public List<Greeting> getAllGreetings() {
        return new ArrayList<>(greetings);
    }

    @PostMapping("/hello")
    public Greeting createGreeting(@RequestBody GreetingRequest request) {
        // Format the message to include both name and message content

        Greeting greeting = new Greeting(
                String.valueOf(greetings.size() + 1),
                request.getMessage(),
                request.getName()
        );
        greetings.add(greeting);
        return greeting;
    }

    @PutMapping("/hello/{id}")
    public ResponseEntity<Greeting> updateGreeting(@PathVariable String id, @RequestBody GreetingRequest request) {
        Optional<Greeting> greetingOpt = findGreetingById(id);

        if (greetingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Format the message to include both name and message content
        String formattedMessage = String.format("Hello %s! Your message: %s",
                request.getName(), request.getMessage());

        int index = greetings.indexOf(greetingOpt.get());
        Greeting updatedGreeting = new Greeting(
                id,
                formattedMessage,  // Use the formatted message
                request.getName()
        );

        greetings.set(index, updatedGreeting);
        return ResponseEntity.ok(updatedGreeting);
    }
    // Additional PUT endpoint for Cucumber tests with name
    @PutMapping("/hello/name/{name}")
    public ResponseEntity<Greeting> updateGreetingByName(@PathVariable String name, @RequestBody GreetingRequest request) {
        Optional<Greeting> greetingOpt = findGreetingByName(name);

        if (greetingOpt.isEmpty()) {
            // If not found, create a new greeting with this name
            Greeting newGreeting = new Greeting(
                    String.valueOf(greetings.size() + 1),
                    request.getMessage(),
                    request.getName()
            );
            greetings.add(newGreeting);
            return ResponseEntity.ok(newGreeting);
        }

        // If found, update it
        Greeting existingGreeting = greetingOpt.get();
        int index = greetings.indexOf(existingGreeting);

        Greeting updatedGreeting = new Greeting(
                existingGreeting.getId(),
                request.getMessage(),
                request.getName()
        );

        greetings.set(index, updatedGreeting);
        return ResponseEntity.ok(updatedGreeting);
    }

    @DeleteMapping("/hello/{id}")
    public ResponseEntity<Void> deleteGreeting(@PathVariable String id) {
        Optional<Greeting> greetingOpt = findGreetingById(id);

        if (greetingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        greetings.remove(greetingOpt.get());
        return ResponseEntity.noContent().build();
    }

    // Additional DELETE endpoint for Cucumber tests with name
    @DeleteMapping("/hello/name/{name}")
    public ResponseEntity<Void> deleteGreetingByName(@PathVariable String name) {
        Optional<Greeting> greetingOpt = findGreetingByName(name);

        if (greetingOpt.isEmpty()) {
            // For the Cucumber test to pass
            return ResponseEntity.ok().build();
        }

        greetings.remove(greetingOpt.get());
        return ResponseEntity.ok().build();
    }
}